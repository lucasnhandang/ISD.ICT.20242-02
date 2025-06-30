package com.hustict.aims.service.reservation;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.model.order.OrderItem;
import com.hustict.aims.model.reservation.Reservation;
import com.hustict.aims.repository.ReservationRepository;
import com.hustict.aims.model.reservation.Reservation.Status;
import com.hustict.aims.model.reservation.ReservationItem;
import com.hustict.aims.service.product.InventoryService;
import com.hustict.aims.repository.OrderItemRepository;
import com.hustict.aims.repository.ReservationItemRepository;


import jakarta.servlet.http.HttpSession;
import java.util.List;
@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ReservationItemRepository reservationItemRepository;

    @Transactional
    public void createReservation(CartRequestDTO cart, String sessionId) {
        
        reservationRepository.deleteByStatusAndSessionId(Status.ACTIVE, sessionId);

        Reservation reservation = new Reservation();
        reservation.setSessionId(sessionId);
        reservation.setStatus(Status.ACTIVE);
        reservation.setCreatedAt(LocalDateTime.now());

        List<Map.Entry<Long, Integer>> productQuantities = cart.getProductQuantity();
        if (productQuantities == null || productQuantities.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty. Cannot create reservation.");
        }
        
        for (Map.Entry<Long, Integer> entry : productQuantities) {
            ReservationItem item = new ReservationItem();
            item.setProductId(entry.getKey().longValue()); 
            item.setQuantity(entry.getValue());
            item.setReservation(reservation); 

            reservation.getItems().add(item); 
        }
        
        reservationRepository.save(reservation);

    }
            // sửa

    @Transactional
    public void releaseReservation(HttpSession session) {
        String sessionId = session.getId();

        List<Reservation> reservations = reservationRepository
            .findBySessionIdAndStatus(sessionId, Status.ACTIVE);

        if (reservations.size() > 1) {
            throw new IllegalStateException("Có nhiều hơn một reservation ACTIVE cho session: " + sessionId);
        }

        if (reservations.isEmpty()) {
            throw new IllegalStateException("Không tìm thấy reservation ACTIVE cho session: " + sessionId);
        }

        Reservation reservation = reservations.get(0);

        if (reservation.getStatus() == Status.CONFIRMED) {
            throw new IllegalStateException("Không thể hủy reservation đã xác nhận.");
        }

        if (reservation.getStatus() == Status.RELEASED) {
            throw new IllegalStateException("Reservation đã bị hủy trước đó.");
        }

        InvoiceDTO invoiceDTO = (InvoiceDTO) session.getAttribute("invoice");

        if (invoiceDTO != null && invoiceDTO.getPaymentTransactionId() != null) {
            throw new IllegalStateException("Cannot release reservation.");
        }

        reservation.setStatus(Status.RELEASED); 
        reservationRepository.save(reservation); 
    }

    @Transactional
    public void confirmReservation(HttpSession session, Long orderId) {
        String sessionId = session.getId();

        List<Reservation> reservations = reservationRepository
            .findBySessionIdAndStatus(sessionId, Status.ACTIVE);

        if (reservations.size() > 1) {
            throw new IllegalStateException("Có nhiều hơn một reservation ACTIVE cho session: " + sessionId);
        }

        if (reservations.isEmpty()) {
            throw new IllegalStateException("Không tìm thấy reservation ACTIVE cho session: " + sessionId);
        }

        Reservation reservation = reservations.get(0);

        if (reservation.getStatus() == Status.CONFIRMED) {
            throw new IllegalStateException("Reservation đã được xác nhận và không thể thay đổi.");
        } else if (reservation.getStatus() == Status.RELEASED) {
            throw new IllegalStateException("Reservation đã bị hủy và không thể thay đổi.");
        }
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        if (orderItems.isEmpty()) {
            reservation.setStatus(Status.CONFIRMED);
            reservationRepository.save(reservation);
            System.out.println("Không có OrderItem nào. Reservation đã được xác nhận.");
            return;
        }
        List<ReservationItem> reservationItems = reservationItemRepository.findByReservationId(reservation.getId());

        Map<Long, ReservationItem> reservationItemMap = reservationItems.stream()
            .collect(Collectors.toMap(ReservationItem::getProductId, item -> item));
        for (OrderItem orderItem : orderItems) {
            Long productId = orderItem.getProduct().getId();
            System.out.println("Đang xử lý OrderItem với productId: " + productId);

            if (reservationItemMap.containsKey(productId)) {
                ReservationItem reservationItem = reservationItemMap.get(productId);
                System.out.println("Tìm thấy ReservationItem tương ứng với productId: " + productId);
                System.out.println("Giảm số lượng sản phẩm (productId: " + productId + ") với quantity: " + reservationItem.getQuantity());

                inventoryService.decreaseQuantity(productId, reservationItem.getQuantity());

                System.out.println("Xóa ReservationItem với productId: " + productId);
                reservationItemRepository.delete(reservationItem);
            } else {
                System.out.println("Không tìm thấy ReservationItem cho productId: " + productId);
            }
        }
    

    }

}