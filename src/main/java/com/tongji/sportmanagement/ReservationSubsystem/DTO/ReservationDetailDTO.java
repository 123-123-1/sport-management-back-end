package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import java.util.List;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetailDTO
{
  ReservationBasicDTO basicInfo;
  List<ReservationUserDTO> users;
  List<ReservationRecord> records;
}
