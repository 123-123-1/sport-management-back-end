package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import java.time.Instant;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationState;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationType;

public interface ReservationMetaReflection
{
  Integer getReservationId();
  ReservationState getState();
  String getVenueName();
  String getCourtName();
  ReservationType getType();
  Instant getStartTime();
  Instant getEndTime(); 
}
