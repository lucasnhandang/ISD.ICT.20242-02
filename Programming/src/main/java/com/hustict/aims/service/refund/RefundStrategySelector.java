package com.hustict.aims.service.refund;

import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.payment.PaymentTransaction;
import com.hustict.aims.repository.InvoiceRepository;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.repository.PaymentTransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RefundStrategySelector {
    @Autowired
    private final Map<String, RefundService> refundServiceMap;
    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final InvoiceRepository invoiceRepository;

    @Autowired
    private final PaymentTransactionRepository transactionRepository;

    
    public RefundStrategySelector(List<RefundService> refundServices,
                                  OrderRepository orderRepository,
                                  InvoiceRepository invoiceRepository,
                                  PaymentTransactionRepository transactionRepository) {
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.transactionRepository = transactionRepository;

        this.refundServiceMap = refundServices.stream()
            .collect(Collectors.toMap(
                s -> s.getClass().getAnnotation(Service.class).value(),
                Function.identity()
            ));
    }

     public void refund(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Cannot find order " + orderId));

        Long invoiceId = order.getInvoice().getId();
        Invoice invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new IllegalArgumentException("Cannot find invoice " + invoiceId));

        Long transactionId = invoice.getPaymentTransaction().getId();
        PaymentTransaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new IllegalArgumentException("Cannot find transaction " + transactionId));

        String system = transaction.getSystems(); 

        RefundService selected = refundServiceMap.get(system);

        if (selected == null) {
            throw new IllegalArgumentException("No service supported: " + system);
        }
        else {
            System.out.println("Direct to payment system: "+ system );
        }
        selected.processRefund(orderId);
    }

}