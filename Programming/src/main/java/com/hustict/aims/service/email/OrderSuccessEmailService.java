package com.hustict.aims.service.email;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.email.OrderSuccessEmailRequest;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;


@Service("orderSuccess")
public class OrderSuccessEmailService extends SendEmailServiceImpl<OrderSuccessEmailRequest> {

    @Override
    public OrderSuccessEmailRequest buildRequest(HttpSession session) {
        OrderSuccessEmailRequest req = instantiateRequest();
        populateCommonFields(req);
        req.setCancelLink("http://localhost:3000/order/cancel/" + req.getOrder().getOrderId());
        return req;
    }

    @Override
    protected OrderSuccessEmailRequest instantiateRequest() {
        return new OrderSuccessEmailRequest();
    }

    @Override
    protected String buildSubject(OrderSuccessEmailRequest request) {
        return "Your Order #" + request.getOrder().getOrderId() + " is Confirmed!";
    }

    @Override
    protected String buildBody(OrderSuccessEmailRequest request) {
        var info = request.getOrder();
        var delivery = request.getDeliveryInfor();
        var items = info.getProductList();
        StringBuilder body = new StringBuilder();

        body.append("Dear ")
            .append(delivery.getCustomerName())
            .append(",\n\n");

        body.append("Thank you for your purchase. Here are your order details:\n");
        for (CartItemRequestDTO item : items) {
            body.append(String.format("- %s x%d: %d VND\n",
                    item.getProductName(), item.getQuantity(), item.getPrice()));
        }

        body.append(String.format("\nDelivery Address: %s, %s\n",
                delivery.getDeliveryAddress(), delivery.getDeliveryProvince()));

        if (delivery.isRushOrder()) {
            body.append("Note: This is a rush order.\n");
        }

        body.append(String.format("Expected Delivery Date: %s\n", delivery.getExpectedDate()));
        body.append(String.format("\nIf you wish to cancel, click here: %s\n", request.getCancelLink()));
        body.append("\nBest regards,\nYour Shop Team");

        return body.toString();
    }

}