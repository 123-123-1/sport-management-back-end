package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationGroupDTO
{
  Integer groupReservationId;
  Integer groupId;
  String groupName;
}
