package com.hustict.aims.service.reservation;

import com.hustict.aims.model.reservation.Reservation;
import com.hustict.aims.model.reservation.Reservation.Status;
import com.hustict.aims.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationCleanupScheduler {

    private static final int RESERVATION_TIMEOUT_MINUTES = 30;

    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional
    @Scheduled(fixedRate = 5 * 60 * 1000) 
    public void autoReleaseExpiredReservations() {
        LocalDateTime expireTime = LocalDateTime.now().minusMinutes(RESERVATION_TIMEOUT_MINUTES);

        List<Reservation> expiredReservations =
                reservationRepository.findByStatusAndCreatedAtBefore(Status.ACTIVE, expireTime);

        for (Reservation reservation : expiredReservations) {
            reservation.setStatus(Status.RELEASED);
            System.out.println("[AUTO RELEASE] Reservation ID " + reservation.getId() + " đã hết hạn và bị huỷ.");
        }

        reservationRepository.saveAll(expiredReservations);
    }
}