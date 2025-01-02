package com.tongji.sportmanagement.ReservationSubsystem.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group_reservation")
public class GroupReservation
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_reservation_id", nullable = false)
  Integer groupReservationId;

  @Column(name = "group_id")
  Integer groupId;

  @OneToOne
  @JoinColumn(name = "reservation_id", nullable = false, updatable = false)
  Reservation reservation;
}
