package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourtAvailabilityConfigDTO
{
  Integer courtId;
  String courtName;
  List<AvailabilityConfigInfoDTO> config;
}
