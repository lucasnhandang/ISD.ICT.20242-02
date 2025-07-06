package com.hustict.aims.service.payment;

import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.model.payment.PaymentTransaction;
import com.hustict.aims.model.order.Order;

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


public interface SavePaymentTransaction {

      public OrderDTO save(PaymentTransactionDTO paymentTransaction, Long id);
}
