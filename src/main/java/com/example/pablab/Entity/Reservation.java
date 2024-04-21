package com.example.pablab.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReservationID")
    private Long reservationID;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "roomID")
    private Room room;

    @Column(nullable = false)
    private Date checkInDate;

    @Column(nullable = false)
    private Date checkOutDate;

    @Column(nullable = false)
    private double totalPrice;

    @Column(name = "ReservationStatus")
    private String reservationStatus;
}
