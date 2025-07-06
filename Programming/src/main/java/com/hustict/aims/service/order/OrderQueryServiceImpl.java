package com.hustict.aims.service.order;

import com.hustict.aims.exception.OrderOperationException;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.order.OrderStatus;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.utils.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderQueryServiceImpl implements OrderQueryService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderMapper orderMapper;

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
    @Transactional(readOnly = true)
    public OrderInformationDTO getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderOperationException("Order not found with id: " + orderId));
        return orderMapper.toDTO(order);
    }
} 