package com.hustict.aims.service.placeRushOrder;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.order.RushOrderResponseDTO;
import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.service.placeOrder.InoviceService;
import com.hustict.aims.service.shippingFeeCalculator.ShippingFeeCalculator;
import com.hustict.aims.service.invoice.InvoiceCalculationService;
import com.hustict.aims.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Service
public class RushOrderProcessingService {
    
    private final InoviceService normalOrderService;
    private final ShippingFeeCalculator rushShippingFeeCalculator;
    private final InvoiceCalculationService invoiceCalculationService;
    private final MessageService messageService;

    @Autowired
    public RushOrderProcessingService(InoviceService normalOrderService,
                                    @Qualifier("rushShippingFee") ShippingFeeCalculator rushShippingFeeCalculator,
                                    InvoiceCalculationService invoiceCalculationService,
                                    MessageService messageService) {
        this.normalOrderService = normalOrderService;
        this.rushShippingFeeCalculator = rushShippingFeeCalculator;
        this.invoiceCalculationService = invoiceCalculationService;
        this.messageService = messageService;
    }

    public RushOrderResponseDTO processRushOrder(CartRequestDTO cart, 
                                                DeliveryFormDTO deliveryInfo,
                                                List<CartItemRequestDTO> rushItems,
                                                List<CartItemRequestDTO> normalItems,
                                                HttpSession session) {
        List<InvoiceDTO> invoiceList = new java.util.ArrayList<>();
        List<CartRequestDTO> cartList = new java.util.ArrayList<>();

        // Xử lý rush order
        CartRequestDTO rushCart = createRushCart(rushItems, cart);
        String province = deliveryInfo.getDeliveryProvince();
        
        // Debug log
        System.out.println("🔍 Debug RushOrderProcessingService - Rush Cart Total Price: " + rushCart.getTotalPrice());
        System.out.println("🔍 Debug RushOrderProcessingService - Rush Items: " + rushItems);
        
        int rushShippingFee = rushShippingFeeCalculator.calculateShippingFee(province, rushCart.getProductList(), rushCart.getTotalPrice());
        InvoiceDTO rushInvoiceDTO = invoiceCalculationService.calculateInvoice(rushCart.getTotalPrice(), rushShippingFee);
        rushInvoiceDTO.setRushOrder(true);
        invoiceList.add(rushInvoiceDTO);
        cartList.add(rushCart);

        // Xử lý đơn thường nếu có
        if (!normalItems.isEmpty()) {
            CartRequestDTO normalCart = createNormalCart(normalItems, cart);
            InvoiceDTO normalInvoiceDTO = normalOrderService.createInvoice(deliveryInfo, normalCart);
            normalInvoiceDTO.setRushOrder(false);
            invoiceList.add(normalInvoiceDTO);
            cartList.add(normalCart);
        }

        // Lưu vào session
        session.setAttribute("invoiceList", invoiceList);
        session.setAttribute("cartList", cartList);

        // Build response
        return new RushOrderResponseDTO(invoiceList, cartList, "SUCCESS", 
            messageService.getRushOrderSuccess(), deliveryInfo.getExpectedDateTime());
    }

    private CartRequestDTO createRushCart(List<CartItemRequestDTO> rushItems, CartRequestDTO originalCart) {
        CartRequestDTO rushCart = new CartRequestDTO();
        rushCart.setProductList(rushItems);
        rushCart.setTotalItem(rushItems.size());
        rushCart.setCurrency(originalCart.getCurrency());
        rushCart.setDiscount(originalCart.getDiscount());
        rushCart.setTotalPrice(rushItems.stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum());
        rushCart.setRushOrder(true); // Đánh dấu đây là rush order
        return rushCart;
    }

    private CartRequestDTO createNormalCart(List<CartItemRequestDTO> normalItems, CartRequestDTO originalCart) {
        CartRequestDTO normalCart = new CartRequestDTO();
        normalCart.setProductList(normalItems);
        normalCart.setTotalItem(normalItems.size());
        normalCart.setCurrency(originalCart.getCurrency());
        normalCart.setDiscount(originalCart.getDiscount());
        normalCart.setTotalPrice(normalItems.stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum());
        normalCart.setRushOrder(false); // Đánh dấu đây là normal order
        return normalCart;
    }
} 