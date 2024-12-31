package com.tongji.sportmanagement.VenueSubsystem.DTO;

import lombok.Data;

import com.tongji.sportmanagement.Common.DTO.UserProfileDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.VenueComment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentItemDTO
{
  VenueComment commentInfo;
  UserProfileDTO userInfo;
}