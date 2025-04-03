package com.tongji.sportmanagement.VenueSubsystem.DTO;

import lombok.Data;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentItemDTO
{
  Integer commentId;
  String content;
  Instant time;
  Integer venueId;
  Double score;
  Integer userId;
  String userName;
  String userPhoto;
}