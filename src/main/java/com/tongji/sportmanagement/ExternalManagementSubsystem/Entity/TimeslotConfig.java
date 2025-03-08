package com.tongji.sportmanagement.ExternalManagementSubsystem.Entity;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "timeslot_config")
public class TimeslotConfig
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tsconfig_id", nullable = false)
  Integer tsconfigId;

  @Column(name = "start_time")
  LocalTime startTime;

  @Column(name = "end_time")
  LocalTime endTime;

  @Column(name = "avconfig_id")
  Integer avconfigId;

  @Column(name = "price")
  Double price;
}
