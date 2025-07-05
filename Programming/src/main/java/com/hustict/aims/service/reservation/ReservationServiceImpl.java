package com.hustict.aims.service.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.model.order.OrderItem;
import com.hustict.aims.model.reservation.Reservation;
import com.hustict.aims.model.reservation.Reservation.Status;
import com.hustict.aims.model.reservation.ReservationItem;
import com.hustict.aims.repository.OrderItemRepository;
import com.hustict.aims.repository.ReservationItemRepository;
import com.hustict.aims.repository.ReservationRepository;
import com.hustict.aims.service.product.InventoryService;

import jakarta.servlet.http.HttpSession;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ReservationItemRepository reservationItemRepository;

    @Override
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
            item.setProductId(entry.getKey());
            item.setQuantity(entry.getValue());
            item.setReservation(reservation);
            reservation.getItems().add(item);
        }

        reservationRepository.save(reservation);
    }

    @Override
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

    @Override
    @Transactional
    public void confirmReservation(String sessionId, Long orderId) {

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
        List<ReservationItem> reservationItems = reservationItemRepository.findByReservationId(reservation.getId());

        Map<Long, ReservationItem> reservationItemMap = reservationItems.stream()
            .collect(Collectors.toMap(ReservationItem::getProductId, item -> item));

        for (OrderItem orderItem : orderItems) {
            Long productId = orderItem.getProduct().getId();
            System.out.println("Đang xử lý OrderItem với productId: " + productId);

            if (reservationItemMap.containsKey(productId)) {
                ReservationItem reservationItem = reservationItemMap.get(productId);
                System.out.println("(ProductId: " + productId + ") với quantity: " + reservationItem.getQuantity());
                inventoryService.decreaseQuantity(productId, reservationItem.getQuantity());
                System.out.println("Remove item with product id:: " + productId);
                reservationItemRepository.delete(reservationItem);
            } else {
                System.out.println("Cannot match product id: " + productId);
            }
        }

       List<ReservationItem> remainingItems = reservationItemRepository.findByReservationId(reservation.getId());

        if (remainingItems == null || remainingItems.isEmpty()) {
            reservation.setStatus(Status.CONFIRMED);
            reservationRepository.save(reservation);
            System.out.println("Confirm reservation");
        }
        reservationRepository.save(reservation);
    }
}
