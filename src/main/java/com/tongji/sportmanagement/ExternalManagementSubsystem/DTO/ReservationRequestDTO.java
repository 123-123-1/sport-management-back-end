package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import java.util.List;

import com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationUserDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDTO
{
  Integer reservationId;
  CourtAvailability targetAvailability;
  List<ReservationUserDTO> users;
}
