package com.tongji.sportmanagement.VenueSubsystem.DTO;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentDTO
{
  int venueId;
  Double score;
  String content;
  Instant time;
}
