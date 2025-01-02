package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import java.util.List;

import lombok.Data;

@Data
public class GroupRequestDTO
{
  Integer availabilityId;
  Integer groupId;
  List<Integer> users;
}
