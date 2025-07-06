package com.hustict.aims.service.order;

import com.hustict.aims.exception.OrderOperationException;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.service.email.EmailSenderFactory;
import com.hustict.aims.service.email.OrderInfoService;
import com.hustict.aims.dto.order.OrderDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.utils.mapper.OrderMapper;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

@Service
public class OrderEmailServiceImpl implements OrderEmailService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private EmailSenderFactory emailSenderFactory;

    @Override
    public void prepareOrderSessionForEmail(String type, Long orderId, HttpSession session) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderOperationException("Order not found with id: " + orderId));
        
        // Convert and store order information
        OrderInformationDTO orderInfo = orderMapper.toDTO(order);
        session.setAttribute("orderInformation", orderInfo);

        // Store delivery information
        DeliveryFormDTO deliveryInfo = new DeliveryFormDTO();
        deliveryInfo.setCustomerName(order.getDeliveryInfo().getName());
        deliveryInfo.setEmail(order.getDeliveryInfo().getEmail());
        deliveryInfo.setPhoneNumber(order.getDeliveryInfo().getPhoneNumber());
        deliveryInfo.setDeliveryAddress(order.getDeliveryInfo().getAddress());
        deliveryInfo.setDeliveryProvince(order.getDeliveryInfo().getProvince());
        deliveryInfo.setRushOrder(order.getIsRushOrder());
        deliveryInfo.setExpectedDateTime(order.getDeliveryInfo().getExpectedTime());
        session.setAttribute("deliveryForm", deliveryInfo);

        // Store invoice information
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(order.getInvoice().getId());
        invoiceDTO.setProductPriceExVAT(order.getInvoice().getProductPriceExVAT());
        invoiceDTO.setProductPriceIncVAT(order.getInvoice().getProductPriceIncVAT());
        invoiceDTO.setShippingFee(order.getInvoice().getShippingFee());
        invoiceDTO.setTotalAmount(order.getInvoice().getTotalAmount());
        session.setAttribute("invoice", invoiceDTO);

        // If there's payment information, store it too
        if (order.getInvoice().getPaymentTransaction() != null) {
            PaymentTransactionDTO paymentDTO = new PaymentTransactionDTO();
            paymentDTO.setBankTransactionId(order.getInvoice().getPaymentTransaction().getBankTransactionId());
            paymentDTO.setContent(order.getInvoice().getPaymentTransaction().getContent());
            paymentDTO.setPaymentTime(order.getInvoice().getPaymentTransaction().getPaymentTime());
            paymentDTO.setPaymentAmount(order.getInvoice().getPaymentTransaction().getPaymentAmount());
            paymentDTO.setCardType(order.getInvoice().getPaymentTransaction().getCardType());
            paymentDTO.setCurrency(order.getInvoice().getPaymentTransaction().getCurrency());
            session.setAttribute("paymentTransaction", paymentDTO);
        }

        OrderDTO orderDTO = orderInfoService.getOrderDTOByOrderId(orderId);
        emailSenderFactory.process(type, orderDTO);
    }
} 