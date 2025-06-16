package com.hustict.aims.repository;

import com.hustict.aims.model.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findBySessionIdAndStatus(String sessionId, Reservation.Status status);
    void deleteByStatusAndCreatedAtBefore(Reservation.Status status, LocalDateTime time);
    void deleteByStatusAndSessionId(Reservation.Status status, String sessionId);
}
