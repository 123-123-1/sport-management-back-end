package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeReservationStateDTO
{
  Integer reservationId;
  ReservationState state;
  Boolean changeAvailability;
}
