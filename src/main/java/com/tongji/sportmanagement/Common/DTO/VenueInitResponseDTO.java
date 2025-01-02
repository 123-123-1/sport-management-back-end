package com.tongji.sportmanagement.Common.DTO;

import java.util.List;

import com.tongji.sportmanagement.VenueSubsystem.DTO.CourtResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueInitResponseDTO
{
  int venueId;
  List<CourtResponseDTO> courts;
  String token;
}
