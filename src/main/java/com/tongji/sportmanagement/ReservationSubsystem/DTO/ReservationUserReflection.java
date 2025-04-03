package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationState;

public interface ReservationUserReflection
{
  Integer getUserReservationId();
  Integer getUserId();
  String getUserName();
  ReservationState getState();
}
