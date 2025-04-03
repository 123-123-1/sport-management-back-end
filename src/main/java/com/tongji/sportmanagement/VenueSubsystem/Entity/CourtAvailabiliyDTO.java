package com.tongji.sportmanagement.VenueSubsystem.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourtAvailabiliyDTO
{
  Integer availabilityId;
  Integer timeslotId;
  Integer courtId;
  CourtAvailabilityState state;
  Double price;
}
