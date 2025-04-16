package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import java.time.Instant;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationState;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationManagerMetaDTO
{
  Integer reservationId;
  Integer courtId;
  String courtName;
  Instant startTime;
  Instant endTime;
  ReservationType type;
  ReservationState state;
}
