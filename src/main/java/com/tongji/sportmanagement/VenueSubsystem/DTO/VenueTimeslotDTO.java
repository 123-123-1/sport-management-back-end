package com.tongji.sportmanagement.VenueSubsystem.DTO;

import java.util.List;

import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Timeslot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueTimeslotDTO
{
  Timeslot timeslot;
  List<CourtAvailability> courtAvailabilities;
}
