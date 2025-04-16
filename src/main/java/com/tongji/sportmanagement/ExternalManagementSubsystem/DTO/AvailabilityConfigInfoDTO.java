package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import java.util.List;

import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.TimeslotConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityConfigInfoDTO
{
  Integer avconfigId;
  String avconfigName;
  Integer courtId;
  List<TimeslotConfig> tsconfig;
  List<String> repetition;
  Integer dayAhead;
  Integer createHour;
}
