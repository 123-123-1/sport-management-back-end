package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import java.time.Instant;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationState;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 预约基本信息
@Data
@AllArgsConstructor
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
  Integer groupId; // 团体预约时不为null
  String groupName; // 团体预约时不为null
  Instant expirationTime; // 拼场预约时不为null
  Integer reservedCount; // 已预约人数
}
