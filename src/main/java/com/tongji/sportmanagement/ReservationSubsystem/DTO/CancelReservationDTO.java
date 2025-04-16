package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelReservationDTO
{
  Integer userId;
  Integer reservationId;
  CancelReservationType type;
}
