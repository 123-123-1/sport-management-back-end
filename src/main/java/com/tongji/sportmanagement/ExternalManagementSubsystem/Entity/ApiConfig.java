package com.tongji.sportmanagement.ExternalManagementSubsystem.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "api_config")
public class ApiConfig
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "api_config_id")
  Integer apiconfigId;

  @Column(name = "venue_id")
  Integer venueId;

  @Enumerated(EnumType.STRING)
  ApiType type;

  @Column(name = "content", length = 1000)
  String content;
}
