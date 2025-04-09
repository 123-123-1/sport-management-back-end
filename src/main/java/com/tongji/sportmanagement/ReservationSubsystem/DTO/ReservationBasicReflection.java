package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import java.time.Instant;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationUserState;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationType;

public interface ReservationBasicReflection
{
  Integer getReservationId();
  Integer getVenueId();
  String getVenueName();
  Integer getCourtId();
  String getCourtName();
  Instant getStartTime();
  Instant getEndTime();
  ReservationType getType();
  ReservationUserState getUserState();
  Integer getGroupId(); // 团体预约时不为null
  String getGroupName(); // 团体预约时不为null
  Instant getExpirationTime(); // 拼场预约时不为null
  Integer getReservedCount(); // 已预约人数
}
