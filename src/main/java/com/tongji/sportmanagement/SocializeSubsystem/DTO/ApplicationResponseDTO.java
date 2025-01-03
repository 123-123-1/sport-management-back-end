package com.tongji.sportmanagement.SocializeSubsystem.DTO;

import java.time.Instant;

import com.tongji.sportmanagement.SocializeSubsystem.Entity.FriendApplicationState;

import lombok.Data;

@Data
public class ApplicationResponseDTO
{
  Integer friendApplicationId;
  String applyInfo;
  FriendApplicationState state;
  Instant operationTime;
  Instant expirationTime;
  Integer applicantId;
  String applicantName;
  Integer reviewerId;
  String reviewerName;
}
