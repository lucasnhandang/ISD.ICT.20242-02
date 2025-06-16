package com.hustict.aims.service.reservation;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.model.reservation.Reservation;
import com.hustict.aims.repository.ReservationRepository;
import com.hustict.aims.model.reservation.Reservation.Status;
import com.hustict.aims.model.reservation.ReservationItem;


//import jakarta.servlet.http.HttpSession;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    // sửa: http session
    public ReservationService(ReservationRepository reservationRepository/* , HttpSession session*/) {
        this.reservationRepository = reservationRepository;
        //this.session = session;
    }
    @Transactional
    public void createReservation(CartRequestDTO cart, String sessionId) {
        // sửa
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
        // 4. Lưu reservation
        reservationRepository.save(reservation);

        // xóa
        System.out.println("Reservation created for cart: ");
    }
            // sửa

    public void releaseReservation(Long reservationId) {
        // Nếu khách hủy hoặc quá hạn, trả lại số lượng
    }
        // sửa

    public void confirmReservation(Long reservationId) {
        // Khi khách thanh toán thành công, confirm để tạo đơn chính thức và trừ tồn kho
    }
}
