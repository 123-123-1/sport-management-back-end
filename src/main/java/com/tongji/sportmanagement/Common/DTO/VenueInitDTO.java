package com.tongji.sportmanagement.Common.DTO;

import java.util.List;

import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueInitDTO
{
  Venue venueInfo;
  List<Court> courts;
}
