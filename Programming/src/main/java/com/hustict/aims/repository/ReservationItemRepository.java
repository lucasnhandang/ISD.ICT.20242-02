package com.hustict.aims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.hustict.aims.model.reservation.ReservationItem;
@Repository
public interface ReservationItemRepository extends JpaRepository<ReservationItem, Long> {

    @Query("SELECT COALESCE(SUM(ri.quantity), 0) " +
            "FROM ReservationItem ri " +
            "JOIN ri.reservation r " +
            "WHERE ri.productId = :productId " +
            "AND r.status = 'ACTIVE'")
    int getReservedQuantityByProductId(@Param("productId") Long productId);

  
    List<ReservationItem> findByReservationId(Long reservationId);

}
