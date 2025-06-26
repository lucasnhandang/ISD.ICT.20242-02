package com.hustict.aims.service.sessionValidator;

import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.exception.BadSessionDataException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SessionValidatorService {

    public void validateDeliveryAndCartForCheckout(HttpSession session) {
        List<String> missing = new ArrayList<>();

        DeliveryFormDTO deliveryForm = (DeliveryFormDTO) session.getAttribute("deliveryForm");
        CartRequestDTO cart = (CartRequestDTO) session.getAttribute("cartRequested");

        if (deliveryForm == null) missing.add("deliveryForm");
        if (cart == null) missing.add("cartRequested");

        if (!missing.isEmpty()) {
            throw new BadSessionDataException(missing);
        }
    }

    public void validateCartRequestedPresent(HttpSession session) {
        List<String> missing = new ArrayList<>();

        CartRequestDTO cart = (CartRequestDTO) session.getAttribute("cartRequested");
        if (cart == null || cart.getProductList() == null || cart.getProductList().isEmpty()) {
            missing.add("cartRequested");
        }

        if (!missing.isEmpty()) {
            throw new BadSessionDataException(missing);
        }
    }

    public void validateAfterPayment(HttpSession session) {
        List<String> missingFields = new ArrayList<>();

        DeliveryFormDTO deliveryForm = (DeliveryFormDTO) session.getAttribute("deliveryForm");
        CartRequestDTO cart = (CartRequestDTO) session.getAttribute("cartRequested");
        InvoiceDTO invoice = (InvoiceDTO) session.getAttribute("invoice");
        PaymentTransactionDTO paymentTransaction = (PaymentTransactionDTO) session.getAttribute("paymentTransaction");

        if (deliveryForm == null) missingFields.add("deliveryForm");
        if (cart == null) missingFields.add("cartRequested");
        if (invoice == null) missingFields.add("invoice");
        if (paymentTransaction == null) missingFields.add("paymentTransaction");

        if (!missingFields.isEmpty()) {
            throw new BadSessionDataException(missingFields);
        }
    }
}
