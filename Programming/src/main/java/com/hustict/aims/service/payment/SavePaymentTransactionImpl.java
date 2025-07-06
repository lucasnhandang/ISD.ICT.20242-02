package com.hustict.aims.service.payment;

import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.model.payment.PaymentTransaction;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.order.OrderStatus;
import com.hustict.aims.dto.order.OrderDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.repository.PaymentTransactionRepository;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.repository.InvoiceRepository;
import com.hustict.aims.utils.mapper.PaymentTransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Service 
public class SavePaymentTransactionImpl implements SavePaymentTransaction {

    private static final Logger logger = LoggerFactory.getLogger(SavePaymentTransaction.class);

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public OrderDTO save(PaymentTransactionDTO paymentTransaction, Long id) {
        
        PaymentTransaction txn = paymentTransactionMapper.toEntity(paymentTransaction);
        
        PaymentTransaction paymentTransactionEntity = paymentTransactionRepository.save(txn);
        Long paymentid = paymentTransactionEntity.getId();

        if ( paymentid == null) {
            throw new IllegalArgumentException("SAVE PAYMENT TRANSACTION : Save paymentTransaction failed");
        }

        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            throw new IllegalArgumentException("SAVE PAYMENT TRANSACTION: Cannot find order with: ID: " + id);
        }

        Order order = orderOptional.get();

        order.setOrderStatus(OrderStatus.PENDING);
        orderRepository.save(order);  


        Long invoiceId = order.getInvoice().getId();
        if (invoiceId == null) {
            throw new IllegalArgumentException("SAVE PAYMENT TRANSACTION: Cannot find Invoice of with order ID: " + id);
        }

        Long deliveryid = order.getDeliveryInfo().getId();
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);
        if (!invoiceOptional.isPresent()) {
            logger.error("Không tìm thấy invoice với ID: " + invoiceId);
            throw new IllegalArgumentException("Không tìm thấy invoice với ID: " + invoiceId);
        }

        
        Invoice invoice = invoiceOptional.get();
        invoice.setPaymentTransaction(paymentTransactionEntity);  
        invoiceRepository.save(invoice);

        logger.info("Payment transaction saved successfully with ID: " + paymentid + " to invoice " + invoiceId + "of order "+ id + "Delivery: " + deliveryid);
        OrderDTO orderinfo = new OrderDTO(id,invoiceId,deliveryid,paymentid);
        return orderinfo;
    }
}
