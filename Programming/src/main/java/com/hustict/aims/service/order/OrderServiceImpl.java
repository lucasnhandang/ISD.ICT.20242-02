package com.hustict.aims.service.order;

import com.hustict.aims.exception.OrderOperationException;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.order.OrderStatus;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.utils.mapper.OrderMapper;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    public void approveOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderOperationException("Order not found with id: " + orderId));

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new OrderOperationException("Only PENDING orders can be approved. Current status: " + order.getOrderStatus());
        }
        orderRepository.approveOrderById(orderId);
    }

    @Override
    @Transactional
    public void rejectOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderOperationException("Order not found with id: " + orderId));

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new OrderOperationException("Only PENDING orders can be rejected. Current status: " + order.getOrderStatus());
        }

        orderRepository.rejectOrderById(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderInformationDTO> getPendingOrders(int page, int size) {
        try {
            System.out.println("Bắt đầu lấy danh sách pending orders...");
            // Sử dụng method mới với JOIN FETCH để tránh N+1 query
            List<Order> pendingOrders = orderRepository.findByOrderStatusWithDetails(OrderStatus.PENDING);
            System.out.println("Tìm thấy " + pendingOrders.size() + " pending orders");
            
            List<OrderInformationDTO> result = pendingOrders.stream()
                    .map(orderMapper::toDTO)
                    .collect(Collectors.toList());
                    
            System.out.println("Đã convert thành công " + result.size() + " DTOs");
            return result;
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy pending orders: " + e.getMessage());
            e.printStackTrace();
            throw new OrderOperationException("Không thể lấy danh sách đơn hàng chờ duyệt: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalPendingOrders() {
        return orderRepository.countByOrderStatus(OrderStatus.PENDING);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderOperationException("Order not found with id: " + orderId));

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new OrderOperationException("Only PENDING orders can be cancelled. Current status: " + order.getOrderStatus());
        }

        orderRepository.cancelOrderById(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderInformationDTO getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderOperationException("Order not found with id: " + orderId));
        return orderMapper.toDTO(order);
    }

    @Override
    public void prepareOrderSessionForEmail(Long orderId, HttpSession session) {
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
    }
} 