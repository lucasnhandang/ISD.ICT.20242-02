package com.hustict.aims.service;

import com.hustict.aims.dto.order.RushOrderRequestDTO;
import com.hustict.aims.dto.order.RushOrderResponseDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.service.placeOrder.NormalOrderService;
import com.hustict.aims.service.shippingFeeCalculator.ShippingFeeCalculator;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.service.invoice.InvoiceCalculationService;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.order.OrderStatus;
import com.hustict.aims.model.shipping.DeliveryInfo;
import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.service.placeRushOrder.RushOrderValidator;
import com.hustict.aims.service.placeRushOrder.CartSplitter;
import com.hustict.aims.service.placeRushOrder.CartSplitter.Pair;
import com.hustict.aims.service.placeRushOrder.DeliveryInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class RushOrderServiceImpl implements RushOrderService {
    private final NormalOrderService normalOrderService;
    private final ShippingFeeCalculator rushShippingFeeCalculator;
    private final ProductRepository productRepository;
    private final InvoiceCalculationService invoiceCalculationService;
    private final OrderRepository orderRepository;
    private final RushOrderValidator rushOrderValidator;
    private final CartSplitter cartSplitter;
    private final DeliveryInfoMapper deliveryInfoMapper;

    @Autowired
    public RushOrderServiceImpl(NormalOrderService normalOrderService,
                               @Qualifier("rushShippingFee") ShippingFeeCalculator rushShippingFeeCalculator,
                               ProductRepository productRepository,
                               InvoiceCalculationService invoiceCalculationService,
                               OrderRepository orderRepository,
                               RushOrderValidator rushOrderValidator,
                               CartSplitter cartSplitter,
                               DeliveryInfoMapper deliveryInfoMapper) {
        this.normalOrderService = normalOrderService;
        this.rushShippingFeeCalculator = rushShippingFeeCalculator;
        this.productRepository = productRepository;
        this.invoiceCalculationService = invoiceCalculationService;
        this.orderRepository = orderRepository;
        this.rushOrderValidator = rushOrderValidator;
        this.cartSplitter = cartSplitter;
        this.deliveryInfoMapper = deliveryInfoMapper;
    }

    @Override
    public RushOrderResponseDTO processRushOrder(RushOrderRequestDTO request) {
        CartRequestDTO cart = request.getCart();
        DeliveryFormDTO deliveryInfo = request.getDeliveryInfo();
        // Validate địa chỉ
        if (!rushOrderValidator.validateAddress(deliveryInfo)) {
            return new RushOrderResponseDTO(null, "FAILED", "Rush order chỉ hỗ trợ giao tại Hà Nội");
        }
        // Split cart
        Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> split = cartSplitter.splitRushAndNormal(cart, productRepository);
        List<CartItemRequestDTO> rushItems = split.first;
        List<CartItemRequestDTO> normalItems = split.second;
        if (!rushOrderValidator.validateRushItems(rushItems)) {
            return new RushOrderResponseDTO(null, "FAILED", "Không có sản phẩm nào đủ điều kiện rush order");
        }
        // Prepare rush cart
        CartRequestDTO rushCart = new CartRequestDTO();
        rushCart.setProductList(rushItems);
        rushCart.setTotalItem(rushItems.size());
        rushCart.setCurrency(cart.getCurrency());
        rushCart.setDiscount(cart.getDiscount());
        rushCart.setTotalPrice(rushItems.stream().mapToDouble(CartItemRequestDTO::getPrice).sum());
        // Prepare normal cart
        CartRequestDTO normalCart = new CartRequestDTO();
        normalCart.setProductList(normalItems);
        normalCart.setTotalItem(normalItems.size());
        normalCart.setCurrency(cart.getCurrency());
        normalCart.setDiscount(cart.getDiscount());
        normalCart.setTotalPrice(normalItems.stream().mapToDouble(CartItemRequestDTO::getPrice).sum());
        // Handle rush order
        int rushShippingFee = rushShippingFeeCalculator.calculateShippingFee(deliveryInfo, rushCart);
        InvoiceDTO rushInvoiceDTO = invoiceCalculationService.calculateInvoice((int) rushCart.getTotalPrice(), rushShippingFee);
        // Lưu order rush vào DB
        Order rushOrder = new Order();
        rushOrder.setOrderDate(LocalDateTime.now());
        rushOrder.setOrderStatus(OrderStatus.PENDING);
        rushOrder.setIsRushOrder(true);
        rushOrder.setCurrency(rushCart.getCurrency());
        // Mapping DeliveryInfo
        DeliveryInfo deliveryEntity = deliveryInfoMapper.mapFromDTO(deliveryInfo);
        rushOrder.setDeliveryInfo(deliveryEntity);
        // Mapping Invoice
        Invoice rushInvoiceEntity = new Invoice();
        rushInvoiceEntity.setProductPriceExVAT(rushInvoiceDTO.getTotalPriceExVat());
        rushInvoiceEntity.setProductPriceIncVAT(rushInvoiceDTO.getTotalPriceIncludingVat());
        rushInvoiceEntity.setShippingFee(rushInvoiceDTO.getShippingFee());
        rushInvoiceEntity.setTotalAmount(rushInvoiceDTO.getTotalAmount());
        rushOrder.setInvoice(rushInvoiceEntity);
        orderRepository.save(rushOrder);
        // Handle normal order
        if (!normalItems.isEmpty()) {
            normalOrderService.handleNormalOrder(deliveryInfo, normalCart);
        }
        // Build response
        return new RushOrderResponseDTO(rushInvoiceDTO, "SUCCESS", "Rush order placed successfully");
    }
} 