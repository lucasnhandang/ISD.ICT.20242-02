package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;

import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.payment.PaymentTransaction;
import com.hustict.aims.model.shipping.DeliveryInfo;
import com.hustict.aims.repository.InvoiceRepository;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.repository.PaymentTransactionRepository;
import com.hustict.aims.service.reservation.ReservationService;
import com.hustict.aims.service.sessionValidator.SessionValidatorService;
import com.hustict.aims.utils.mapper.DeliveryInfoMapper;
import com.hustict.aims.utils.mapper.InvoiceMapper;
import com.hustict.aims.utils.mapper.OrderInformationDTOMapper;
import com.hustict.aims.utils.mapper.OrderMapper;
import com.hustict.aims.utils.mapper.PaymentTransactionMapper;
import com.hustict.aims.repository.DeliveryInfoRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SaveOrderService {

    private final OrderRepository orderRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final InvoiceRepository invoiceRepository;
    private final OrderMapper orderMapper;
    private final DeliveryInfoMapper deliveryInfoMapper;
    private final PaymentTransactionMapper paymentTxnMapper;
    private final InvoiceMapper invoiceMapper;
    private final SessionValidatorService sessionValidatorService;
    private final DeliveryInfoRepository deliveryInfoRepository;

 
    public SaveOrderService(
        OrderRepository orderRepository,
        PaymentTransactionRepository transactionRepository,
        InvoiceRepository invoiceRepository,
        OrderMapper orderMapper,
        DeliveryInfoMapper deliveryInfoMapper,
        PaymentTransactionMapper paymentTxnMapper,
        InvoiceMapper invoiceMapper,
        SessionValidatorService sessionValidatorService,
        DeliveryInfoRepository deliveryInfoRepository
    ) {
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
        this.invoiceRepository = invoiceRepository;
        this.orderMapper = orderMapper;
        this.deliveryInfoMapper = deliveryInfoMapper;
        this.paymentTxnMapper = paymentTxnMapper;
        this.invoiceMapper = invoiceMapper;
        this.sessionValidatorService = sessionValidatorService;
        this.deliveryInfoRepository = deliveryInfoRepository;
    }

   
    @Transactional
    public void saveAll(Long orderid ) {
       
    }
}

