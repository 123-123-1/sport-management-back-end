package com.tongji.sportmanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "match_reservation")
public class MatchReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "expiration_time")
    private Instant expirationTime;

    @Column(name = "reserved_count")
    private Integer reservedCount;

}