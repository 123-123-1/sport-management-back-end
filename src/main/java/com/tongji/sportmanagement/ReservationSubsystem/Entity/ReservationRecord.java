package com.tongji.sportmanagement.ReservationSubsystem.Entity;

import java.time.Instant;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation_record")
public class ReservationRecord
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_record_id")
  Integer reservationRecordId;

  @Enumerated(EnumType.STRING)
  @ColumnDefault("reserved")
  @Column(name = "state")
  ReservationState state;

  @Column(name = "time")
  Instant time;

  @Column(name = "user_id")
  Integer userId;

  @Column(name = "reservation_id")
  Integer reservationId;
}
