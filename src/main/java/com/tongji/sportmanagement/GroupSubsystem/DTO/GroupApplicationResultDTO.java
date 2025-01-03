package com.tongji.sportmanagement.GroupSubsystem.DTO;

import java.time.Instant;

import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupApplicationState;
import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupApplicationType;

import lombok.Data;

@Data
public class GroupApplicationResultDTO
{
  Integer groupApplicationId;
  GroupApplicationType type;
  String applyInfo;
  GroupApplicationState state;
  Instant operationTime;
  Instant expirationTime;
  Integer applicantId;
  String applicantName;
  Integer groupId;
  String groupName;
  Integer reviewerId;
  String reviewerName;
}
