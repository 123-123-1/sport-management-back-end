package com.tongji.sportmanagement.VenueSubsystem.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueCommentDTO
{
  long total;
  long page;
  List<CommentItemDTO> comments;
}
