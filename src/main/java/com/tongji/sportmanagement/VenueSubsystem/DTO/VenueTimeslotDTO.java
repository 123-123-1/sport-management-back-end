package com.tongji.sportmanagement.VenueSubsystem.DTO;

import java.time.Instant;
import java.util.List;

import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailabiliyDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueTimeslotDTO
{
  Integer timeslotId;
  Instant startTime;
  Instant endTime;
  List<CourtAvailabiliyDTO> courtAvailabilities;

  // public VenueTimeslotDTO(Integer timeslotId, Instant startTime, Instant endTime, List<CourtAvailability> courtAvailabilities)
  // {
  //   this.timeslotId = timeslotId;
  //   this.startTime = startTime;
  //   this.endTime = endTime;
  //   this.courtAvailabilities = new ArrayList<>(courtAvailabilities);
  // }
}
