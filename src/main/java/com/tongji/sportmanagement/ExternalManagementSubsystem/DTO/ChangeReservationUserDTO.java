package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationUserState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeReservationUserDTO
{
  Integer reservationId;
  Integer userId;
  ReservationUserState userState;
}
