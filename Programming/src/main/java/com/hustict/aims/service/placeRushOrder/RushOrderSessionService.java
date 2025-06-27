package com.hustict.aims.service.placeRushOrder;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.dto.order.RushOrderEligibilityResponseDTO;
import com.hustict.aims.dto.order.RushOrderResponseDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class RushOrderSessionService {

    public void saveRushEligibility(HttpSession session, RushOrderEligibilityResponseDTO eligibility) {
        session.setAttribute("rushEligibility", eligibility);
    }

    public void saveRushDeliveryInfo(HttpSession session, DeliveryFormDTO rushDeliveryInfo) {
        session.setAttribute("rushDeliveryInfo", rushDeliveryInfo);
    }

    public void saveRushOrderResponse(HttpSession session, RushOrderResponseDTO response) {
        session.setAttribute("rushOrderResponse", response);
        session.setAttribute("invoice", response.getInvoice());
    }

    public void saveOrderInformation(HttpSession session, OrderInformationDTO orderInfo) {
        session.setAttribute("orderInformation", orderInfo);
    }

    public RushOrderEligibilityResponseDTO getRushEligibility(HttpSession session) {
        return (RushOrderEligibilityResponseDTO) session.getAttribute("rushEligibility");
    }

    public DeliveryFormDTO getRushDeliveryInfo(HttpSession session) {
        return (DeliveryFormDTO) session.getAttribute("rushDeliveryInfo");
    }

    public RushOrderResponseDTO getRushOrderResponse(HttpSession session) {
        return (RushOrderResponseDTO) session.getAttribute("rushOrderResponse");
    }

    public OrderInformationDTO getOrderInformation(HttpSession session) {
        return (OrderInformationDTO) session.getAttribute("orderInformation");
    }

    public CartRequestDTO getCartRequested(HttpSession session) {
        return (CartRequestDTO) session.getAttribute("cartRequested");
    }

    public DeliveryFormDTO getDeliveryForm(HttpSession session) {
        return (DeliveryFormDTO) session.getAttribute("deliveryForm");
    }

    public InvoiceDTO getInvoice(HttpSession session) {
        return (InvoiceDTO) session.getAttribute("invoice");
    }

    public void clearRushOrderSession(HttpSession session) {
        session.removeAttribute("rushEligibility");
        session.removeAttribute("rushDeliveryInfo");
        session.removeAttribute("rushOrderResponse");
        session.removeAttribute("orderInformation");
        session.removeAttribute("invoice");
    }

    public boolean hasRushEligibility(HttpSession session) {
        return session.getAttribute("rushEligibility") != null;
    }

    public boolean hasRushDeliveryInfo(HttpSession session) {
        return session.getAttribute("rushDeliveryInfo") != null;
    }

    public boolean hasRushOrderResponse(HttpSession session) {
        return session.getAttribute("rushOrderResponse") != null;
    }
} 