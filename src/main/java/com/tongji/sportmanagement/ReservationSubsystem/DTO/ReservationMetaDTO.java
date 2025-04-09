package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import java.time.Instant;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationUserState;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationState;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationMetaDTO
{
  Integer reservationId;
  ReservationState state;
  ReservationUserState userState;
  String venueName;
  String courtName;
  ReservationType type;
  Instant startTime;
  Instant endTime;
}
