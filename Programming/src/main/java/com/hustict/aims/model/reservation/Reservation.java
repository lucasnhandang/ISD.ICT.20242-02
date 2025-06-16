package com.hustict.aims.model.reservation;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationItem> items = new ArrayList<>();

    public List<ReservationItem> getItems() {
        return items;
    }
    
    public enum Status {
        ACTIVE,
        RELEASED,
        CONFIRMED
    }

}
