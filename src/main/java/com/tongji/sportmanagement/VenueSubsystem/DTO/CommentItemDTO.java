package com.tongji.sportmanagement.VenueSubsystem.DTO;

import lombok.Data;

import com.tongji.sportmanagement.VenueSubsystem.Entity.VenueComment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class VenueCommentItem
{
  VenueComment commentInfo;
  
}