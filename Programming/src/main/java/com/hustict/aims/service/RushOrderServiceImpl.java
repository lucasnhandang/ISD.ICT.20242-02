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
import com.hustict.aims.utils.mapper.DeliveryInfoMapper;
import com.hustict.aims.repository.InvoiceRepository;
import com.hustict.aims.exception.RushOrderException;
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
    private final InvoiceRepository invoiceRepository;
    private final RushOrderValidator rushOrderValidator;
    private final CartSplitter cartSplitter;
    private final DeliveryInfoMapper deliveryInfoMapper;
    private final MessageService messageService;

    @Autowired
    public RushOrderServiceImpl(NormalOrderService normalOrderService,
                               @Qualifier("rushShippingFee") ShippingFeeCalculator rushShippingFeeCalculator,
                               ProductRepository productRepository,
                               InvoiceCalculationService invoiceCalculationService,
                               OrderRepository orderRepository,
                               InvoiceRepository invoiceRepository,
                               RushOrderValidator rushOrderValidator,
                               CartSplitter cartSplitter,
                               DeliveryInfoMapper deliveryInfoMapper,
                               MessageService messageService) {
        this.normalOrderService = normalOrderService;
        this.rushShippingFeeCalculator = rushShippingFeeCalculator;
        this.productRepository = productRepository;
        this.invoiceCalculationService = invoiceCalculationService;
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.rushOrderValidator = rushOrderValidator;
        this.cartSplitter = cartSplitter;
        this.deliveryInfoMapper = deliveryInfoMapper;
        this.messageService = messageService;
    }

    @Override
    public RushOrderResponseDTO processRushOrder(RushOrderRequestDTO request) {
        CartRequestDTO cart = request.getCart();
        DeliveryFormDTO deliveryInfo = request.getDeliveryInfo();
        
        // Chỉ kiểm tra sản phẩm hợp lệ rush order
        Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> split = cartSplitter.splitRushAndNormal(cart, productRepository);
        List<CartItemRequestDTO> rushItems = split.first;
        List<CartItemRequestDTO> normalItems = split.second;
        
        if (rushItems == null || rushItems.isEmpty()) {
            throw new RushOrderException(
                messageService.getRushOrderNoEligibleProducts(),
                "NO_ELIGIBLE_PRODUCTS",
                deliveryInfo.getExpectedDateTime()
            );
        }
        // Nếu có sản phẩm rush order, kiểm tra đã có thông tin bổ sung chưa (expectedTime, instructions, ...)
        // (Có thể bổ sung logic kiểm tra các trường này nếu cần)
        // Sau khi đã xác định rushItems không rỗng
        if (deliveryInfo.getExpectedDateTime() == null) {
            throw new RushOrderException(
                "Missing expected delivery time for rush order.",
                "MISSING_EXPECTED_TIME",
                null
            );
        }
        if (deliveryInfo.getDeliveryInstructions() == null || deliveryInfo.getDeliveryInstructions().trim().isEmpty()) {
            throw new RushOrderException(
                "Missing delivery instructions for rush order.",
                "MISSING_DELIVERY_INSTRUCTIONS",
                deliveryInfo.getExpectedDateTime()
            );
        }
        // Xử lý rush order
        CartRequestDTO rushCart = new CartRequestDTO();
        rushCart.setProductList(rushItems);
        rushCart.setTotalItem(rushItems.size());
        rushCart.setCurrency(cart.getCurrency());
        rushCart.setDiscount(cart.getDiscount());
        rushCart.setTotalPrice(rushItems.stream().mapToDouble(CartItemRequestDTO::getPrice).sum());
        
        // Xử lý đơn thường nếu có
        if (!normalItems.isEmpty()) {
            CartRequestDTO normalCart = new CartRequestDTO();
            normalCart.setProductList(normalItems);
            normalCart.setTotalItem(normalItems.size());
            normalCart.setCurrency(cart.getCurrency());
            normalCart.setDiscount(cart.getDiscount());
            normalCart.setTotalPrice(normalItems.stream().mapToDouble(CartItemRequestDTO::getPrice).sum());
            normalOrderService.handleNormalOrder(deliveryInfo, normalCart);
        }
        
        // Tính phí và tạo invoice cho rush order
        int rushShippingFee = rushShippingFeeCalculator.calculateShippingFee(deliveryInfo, rushCart);
        InvoiceDTO rushInvoiceDTO = invoiceCalculationService.calculateInvoice((int) rushCart.getTotalPrice(), rushShippingFee);
        
        // Lưu order rush vào DB
        Order rushOrder = new Order();
        rushOrder.setOrderDate(LocalDateTime.now());
        rushOrder.setOrderStatus(OrderStatus.PENDING);
        rushOrder.setIsRushOrder(true);
        rushOrder.setCurrency(rushCart.getCurrency());
        
        // Mapping DeliveryInfo
        DeliveryInfo deliveryEntity = deliveryInfoMapper.toEntity(deliveryInfo);
        deliveryEntity.setExpectedTime(deliveryInfo.getExpectedDateTime());
        rushOrder.setDeliveryInfo(deliveryEntity);
        
        // Mapping Invoice
        Invoice rushInvoiceEntity = new Invoice();
        rushInvoiceEntity.setProductPriceExVAT(rushInvoiceDTO.getProductPriceExVAT());
        rushInvoiceEntity.setProductPriceIncVAT(rushInvoiceDTO.getProductPriceIncVAT());
        rushInvoiceEntity.setShippingFee(rushInvoiceDTO.getShippingFee());
        rushInvoiceEntity.setTotalAmount(rushInvoiceDTO.getTotalAmount());
        
        // Lưu invoice trước
        invoiceRepository.save(rushInvoiceEntity);
        rushOrder.setInvoice(rushInvoiceEntity);
        orderRepository.save(rushOrder);
        
        // Build response
        return new RushOrderResponseDTO(rushInvoiceDTO, "SUCCESS", messageService.getRushOrderSuccess(), deliveryInfo.getExpectedDateTime());
    }
} 