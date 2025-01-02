package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import java.util.List;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.Reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndividualResponseDTO
{
  Reservation reservationInfo;
  List<ReservationUserDTO> users;
}
