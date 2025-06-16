package com.hustict.aims.model.reservation;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ReservationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

}