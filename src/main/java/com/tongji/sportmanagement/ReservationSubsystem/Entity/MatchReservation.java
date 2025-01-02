package com.tongji.sportmanagement.ReservationSubsystem.Entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "match_reservation")
public class MatchReservation
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "match_reservation_id", nullable = false)
  Integer matchReservationId;

  @Column(name = "reservation_id")
  Integer reservationId;

  @Column(name = "expiration_time")
  Instant expirationTime;

  @Column(name = "reserved_count")
  Integer reservedCount;
}
