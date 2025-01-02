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
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_reservation")
public class UserReservation
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_reservation_id")
  private Integer userReservationId;

  @Column(name = "user_id")
  private Integer userId;

  @OneToOne
  @JoinColumn(name = "reservation_id", nullable = false, updatable = false)
  private Reservation reservation;
}