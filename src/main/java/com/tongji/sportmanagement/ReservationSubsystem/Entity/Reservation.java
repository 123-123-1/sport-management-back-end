package com.tongji.sportmanagement.ReservationSubsystem.Entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservation")
public class Reservation
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_id", nullable = false)
  private Integer reservationId;

  @Enumerated(EnumType.STRING)
  @ColumnDefault("individual")
  @Column(name = "type")
  private ReservationType type;

  @Enumerated(EnumType.STRING)
  @ColumnDefault("reserved")
  @Column(name = "state")
  private ReservationState state;

  @Column(name = "availability_id")
  private Integer availabilityId;
}
