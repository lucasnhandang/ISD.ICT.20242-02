package com.hustict.aims.service.email;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.email.OrderSuccessEmailRequest;
import com.hustict.aims.dto.order.OrderDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;


import java.text.DecimalFormat;

import org.springframework.stereotype.Service;


@Service("orderSuccess")
public class OrderSuccessEmailService extends SendEmailServiceImpl<OrderSuccessEmailRequest> {

    @Override
    public OrderSuccessEmailRequest buildRequest(OrderDTO order) {
        OrderSuccessEmailRequest req = instantiateRequest();
        populateCommonFields(req, order);
        req.setCancelLink("http://localhost:3000/order/cancel/" + req.getOrder().getOrderId());
        return req;
    }

    @Override
    protected OrderSuccessEmailRequest instantiateRequest() {
        return new OrderSuccessEmailRequest();
    }

    @Override
    protected String buildSubject(OrderSuccessEmailRequest request, Long orderId) {
        return "Your Order #" + orderId + " is Pending";
    }
    @Override
    protected String buildBody(OrderSuccessEmailRequest request) {
        var info = request.getOrder();
        var delivery = request.getDeliveryInfor();
        var items = info.getProductList();
        var invoice = request.getInvoice();
        var paymentTransaction = request.getPayment(); // Lấy thông tin thanh toán từ request
        StringBuilder body = new StringBuilder();

        // Định dạng tiền tệ
        DecimalFormat formatter = new DecimalFormat("#,###");

        body.append("<html><body>");
        body.append("<p>Dear ").append(delivery.getCustomerName()).append(",</p>");

        body.append("<p>Thank you for your purchase. Here are your order details:</p>");

        // Create a table to list products
        body.append("<table border='1' cellpadding='5' cellspacing='0'>");
        body.append("<tr><th>Product</th><th>Quantity</th><th>Price (VND)</th></tr>");
        
        // Loop through items and display each in a table row
        for (CartItemRequestDTO item : items) {
            body.append(String.format("<tr><td>%s</td><td>%d</td><td>%s VND</td></tr>",
                    item.getProductName(), item.getQuantity(), formatter.format(item.getPrice())));
        }
        body.append("</table>");

        // Additional delivery and rush order information
        body.append("<p><strong>Delivery Address:</strong> ").append(delivery.getDeliveryAddress()).append(", ")
            .append(delivery.getDeliveryProvince()).append("</p>");
        
        if (delivery.isRushOrder()) {
            body.append("<p><strong>Note:</strong> This is a rush order.</p>");
        }

        body.append("<p><strong>Expected Delivery Date:</strong> ").append(delivery.getExpectedDate()).append("</p>");

        // Add Pending state information and cancel link
        body.append("<p>The order will be in a <strong>pending processing state</strong>, and the software will send invoice and payment transaction information to your email.</p>");
        body.append(String.format("<p>If you wish to cancel, click here: <a href='%s'>Cancel Order</a></p>", request.getCancelLink()));

        // Add Invoice details
        body.append("<p><strong>Invoice Details:</strong></p>");
        body.append("<table border='1' cellpadding='5' cellspacing='0'>");
        body.append("<tr><th>Invoice Number</th><th>Total Amount (Excluding VAT) (VND)</th><th>Total Amount (Including VAT)</th><th>Shipping Fee (VND)</th><th>Total Amount (VND)</th></tr>");
        body.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
            invoice.getId(), formatter.format(invoice.getProductPriceExVAT()), 
            formatter.format(invoice.getProductPriceIncVAT()), 
            formatter.format(invoice.getShippingFee()),
            formatter.format(invoice.getTotalAmount())));  

        body.append("</table>");

        // Add Payment Transaction details
        if (paymentTransaction != null) {
            body.append("<p><strong>Payment Transaction Details:</strong></p>");
            body.append("<table border='1' cellpadding='5' cellspacing='0'>");
            body.append("<tr><th>Bank Transaction ID</th><th>Content</th><th>Payment Time</th><th>Payment Amount (VND)</th><th>Card Type</th><th>Currency</th></tr>");
            body.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                    paymentTransaction.getBankTransactionId(), paymentTransaction.getContent(), paymentTransaction.getPaymentTime(),
                    formatter.format(paymentTransaction.getPaymentAmount()), paymentTransaction.getCardType(), paymentTransaction.getCurrency()));
            body.append("</table>");
        }

        // Closing email
        body.append("<p>Best regards,<br>Your Shop Team</p>");
        body.append("</body></html>");

        return body.toString();
    }



}