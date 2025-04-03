package com.tongji.sportmanagement.VenueSubsystem.DTO;

import java.time.Instant;

public interface CommentItemReflection
{
  Integer getCommentId();
  String getContent();
  Instant getTime();
  Integer getVenueId();
  Double getScore();
  Integer getUserId();
  String getUserName();
}
