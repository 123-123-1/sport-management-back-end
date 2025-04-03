package com.tongji.sportmanagement.VenueSubsystem.DTO;

import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourtDTO
{
  Integer courtId;
  String courtName;
  String location;
  String type;
  Integer capacity;
  CourtState state;
  Integer venueId;
}
