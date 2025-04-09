package com.tongji.sportmanagement.ReservationSubsystem.Entity;

import org.hibernate.annotations.ColumnDefault;

import com.tongji.sportmanagement.AccountSubsystem.Entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

  @Enumerated(EnumType.STRING)
  @ColumnDefault("reserved")
  @Column(name = "user_state")
  private ReservationUserState userState;

  @Column(name = "reservation_id")
  Integer reservationId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "reservation_id",
      referencedColumnName = "reservation_id",
      insertable = false,
      updatable = false
  )
  Reservation reservation;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      referencedColumnName = "user_id",
      insertable = false,
      updatable = false
  )
  User user;

  public UserReservation(Integer userReservationId, Integer userId, ReservationUserState userState, Integer reservationId)
  {
    this.userReservationId = userReservationId;
    this.userId = userId;
    this.userState = userState;
    this.reservationId = reservationId;
    this.reservation = null;
  }
}
