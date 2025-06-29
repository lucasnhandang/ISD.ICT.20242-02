package com.hustict.aims.service.email;

import java.text.DecimalFormat;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.email.CancelOrderEmailRequest;
import com.hustict.aims.dto.order.OrderDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import org.springframework.stereotype.Service;

@Service("cancelOrder")
public class CancelOrderEmailService extends SendEmailServiceImpl<CancelOrderEmailRequest> {

    @Override
    public CancelOrderEmailRequest buildRequest(OrderDTO order) {
        CancelOrderEmailRequest req = instantiateRequest();
        populateCommonFields(req, order);
        return req;
    }

    @Override
    protected CancelOrderEmailRequest instantiateRequest() {
        return new CancelOrderEmailRequest();
    }

    @Override
    protected String buildSubject(CancelOrderEmailRequest request, Long orderId) {
        return "Your Order #" + orderId + " has been Cancelled";
    }

    @Override
    protected String buildBody(CancelOrderEmailRequest request) {
        var info = request.getOrder();
        var delivery = request.getDeliveryInfor();
        var items = info.getProductList();
        var invoice = request.getInvoice();
        var paymentTransaction = request.getPayment();
        StringBuilder body = new StringBuilder();

        // Định dạng tiền tệ
        DecimalFormat formatter = new DecimalFormat("#,###");

        body.append("<html><body>");
        body.append("<p>Dear ").append(delivery.getCustomerName()).append(",</p>");

        body.append("<p>Your order has been cancelled as requested. Here are the details of your cancelled order:</p>");
        body.append("<p><strong>Note:</strong> If you have made any payment, a refund will be processed automatically.</p>");

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
            body.append("<p><strong>Note:</strong> This was a rush order.</p>");
        }

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

        // Add Payment Transaction details and Refund Information
        if (paymentTransaction != null) {
            body.append("<p><strong>Payment Transaction Details:</strong></p>");
            body.append("<table border='1' cellpadding='5' cellspacing='0'>");
            body.append("<tr><th>Bank Transaction ID</th><th>Content</th><th>Payment Time</th><th>Payment Amount (VND)</th><th>Card Type</th><th>Currency</th></tr>");
            body.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                    paymentTransaction.getBankTransactionId(), paymentTransaction.getContent(), paymentTransaction.getPaymentTime(),
                    formatter.format(paymentTransaction.getPaymentAmount()), paymentTransaction.getCardType(), paymentTransaction.getCurrency()));
            body.append("</table>");

            body.append("<p><strong>Refund Information:</strong></p>");
            body.append("<p>A refund for the above payment will be processed automatically to your original payment method. ");
            body.append("The refund process typically takes 3-5 business days to complete.</p>");
        }

        // Closing email
        body.append("<p>If you have any questions about your cancellation or refund, please contact our customer service.</p>");
        body.append("<p>Best regards,<br>Your Shop Team</p>");
        body.append("</body></html>");

        return body.toString();
    }
}