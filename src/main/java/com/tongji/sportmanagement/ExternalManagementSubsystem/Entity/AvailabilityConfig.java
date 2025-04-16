package com.tongji.sportmanagement.ExternalManagementSubsystem.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "availability_config")
public class AvailabilityConfig
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "avconfig_id")
  Integer avconfigId;

  @Column(name = "avconfig_name", length = 100)
  String avconfigName;

  @Column(name = "court_id")
  Integer courtId;

  @Column(name = "repetition", columnDefinition = "BIT(10)")
  Integer repetition;

  @Column(name = "day_ahead")
  Integer dayAhead;

  @Column(name = "create_hour")
  Integer createHour;
}
