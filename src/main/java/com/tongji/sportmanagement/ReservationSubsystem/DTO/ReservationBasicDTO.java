package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import java.time.Instant;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationUserState;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationState;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationType;

import lombok.Data;
import lombok.NoArgsConstructor;

// 预约基本信息
@Data
@NoArgsConstructor
public class ReservationBasicDTO
{
  Integer reservationId;
  Integer venueId;
  String venueName;
  Integer courtId;
  String courtName;
  Instant startTime;
  Instant endTime;
  ReservationType type;
  ReservationState state;
  ReservationUserState userState;
  Integer groupId; // 团体预约时不为null
  String groupName; // 团体预约时不为null
  Instant expirationTime; // 拼场预约时不为null
  Integer reservedCount; // 已预约人数

  public ReservationBasicDTO(Integer reservationId, Integer venueId, String venueName, Integer courtId,
    String courtName, Instant startTime, Instant endTime, ReservationType type, ReservationState state){
      this.reservationId = reservationId;
      this.venueId = venueId;
      this.venueName = venueName;
      this.courtId = courtId;
      this.courtName = courtName;
      this.startTime = startTime;
      this.endTime = endTime;
      this.type = type;
      this.state = state;
  }
}
