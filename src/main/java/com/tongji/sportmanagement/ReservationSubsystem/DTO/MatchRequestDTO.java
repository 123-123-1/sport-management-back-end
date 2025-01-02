package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import java.util.List;

import lombok.Data;

@Data
public class MatchRequestDTO
{
  Integer venueId;
  Integer timeslotId;
  String courtType;
  List<Integer> users;
  Integer reservationCount;
}
