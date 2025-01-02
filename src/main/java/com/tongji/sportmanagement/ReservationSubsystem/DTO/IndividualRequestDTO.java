package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import java.util.List;

import lombok.Data;

@Data
public class IndividualRequestDTO
{
  Integer availabilityId;
  List<Integer> users;
}
