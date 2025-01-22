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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "violation")
public class Violation
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "violation_id")
  private Integer violationId;

  @Enumerated(EnumType.STRING)
  @ColumnDefault("normal")
  @Column(name = "state")
  private ViolationState state;

  @Column(name = "violation_count")
  private Integer violationCount;

  @Column(name = "unlock_time")
  private Instant unlockTime;

  @Column(name = "update_time")
  private Instant updateTime;

  @Column(name = "user_id")
  private Integer userId;
}
