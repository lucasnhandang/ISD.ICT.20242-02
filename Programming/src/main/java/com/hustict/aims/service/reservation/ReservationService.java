package com.hustict.aims.service.reservation;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.model.reservation.Reservation;
import com.hustict.aims.repository.ReservationRepository;
import com.hustict.aims.model.reservation.Reservation.Status;
import com.hustict.aims.model.reservation.ReservationItem;
import com.hustict.aims.service.product.InventoryService;

import jakarta.servlet.http.HttpSession;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    private final InventoryService inventoryService;

    public ReservationService(ReservationRepository reservationRepository,
                                InventoryService inventoryService) {
        this.reservationRepository = reservationRepository;
        this.inventoryService = inventoryService;
    }

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

        // // xóa
        // System.out.println("Reservation created for cart: ");
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
    public void confirmReservation(HttpSession session) {
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
        
        if (session.getAttribute("invoice") != null) {
            InvoiceDTO invoiceDTO = (InvoiceDTO) session.getAttribute("invoice");
            for (ReservationItem item : reservation.getItems()) {
                inventoryService.decreaseQuantity(item.getProductId(), item.getQuantity());
            }
            if (invoiceDTO.getPaymentTransactionId() != null) {
                reservation.setStatus(Status.CONFIRMED);  
                reservationRepository.save(reservation);
            } else {
                throw new IllegalStateException("Cannot find payment transaction in invoice in session");
            }
        } else {
            throw new IllegalStateException("Cannot find invoice in session.");
        }
    }

}