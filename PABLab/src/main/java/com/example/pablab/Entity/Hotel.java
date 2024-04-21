package com.example.pablab.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId;
    @Column(nullable = false, unique = true)
    private String Name;
    @Column(nullable = false)
    private String Address;
    @Column(nullable = false)
    private String hotelPhoneNumber;
    @Column(nullable = false)
    private String hotelEmail;
}
