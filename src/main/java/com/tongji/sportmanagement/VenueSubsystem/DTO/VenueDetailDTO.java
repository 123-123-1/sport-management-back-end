package com.tongji.sportmanagement.VenueSubsystem.DTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.VenueState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueDetailDTO
{
  private Integer venueId;
  private String venueName;
  private String description;
  private String location;
  private VenueState state;
  private String contactNumber;
  private String image;
}
