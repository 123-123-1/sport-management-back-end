package com.tongji.sportmanagement.ExternalManagementSubsystem.Entity;

import org.hibernate.annotations.ColumnDefault;

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
  @Column(name = "operation_type")
  @ColumnDefault("auto")
  ApiOperationType operationType;

  @Column(name = "api_url", length = 255)
  String apiUrl;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  ApiType type;

  @Column(name = "request_content", length = 1000)
  String requestContent;

  @Column(name = "response_content", length = 1000)
  String responseContent;
}
