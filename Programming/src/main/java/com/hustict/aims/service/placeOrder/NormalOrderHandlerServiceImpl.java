package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.NormalOrderResult;
import com.hustict.aims.service.placeOrder.normal.NormalOrderService;
import com.hustict.aims.service.session.SessionManagementService;
import com.hustict.aims.service.sessionValidator.SessionValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
public class NormalOrderHandlerServiceImpl implements NormalOrderHandlerService {

    @Autowired
    private SessionValidatorService sessionValidatorService;

    @Autowired
    private SessionManagementService sessionManagementService;

    @Autowired
    private NormalOrderService normalService;

    @Override
    public ResponseEntity<Map<String, Object>> handleNormalOrder(HttpSession session, CartRequestDTO cart) {
        sessionValidatorService.validateDeliveryAndCartForCheckout(session);

        DeliveryFormDTO deliveryForm = (DeliveryFormDTO) sessionManagementService.getDeliveryForm(session);
        NormalOrderResult results = normalService.processNormalOrder(cart, deliveryForm);

        InvoiceDTO invoice = results.getInvoice();
        Long orderid = results.getOrderId();
        sessionManagementService.addToCartList(session, cart);
        sessionManagementService.addToInvoiceList(session, invoice);

        Map<String, Object> response = new HashMap<>();
        response.put("cart", cart);
        response.put("invoice", invoice);
        response.put("deliveryForm", deliveryForm);
        response.put("orderid", orderid);

        return ResponseEntity.ok(response);
    }
}
