package com.tongji.sportmanagement.VenueSubsystem.Service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.AccountSubsystem.Service.UserService;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.VenueSubsystem.DTO.CommentItemDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.PostCommentDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.VenueComment;
import com.tongji.sportmanagement.VenueSubsystem.Repository.CommentRepositiory;

@Service
public class CommentService
{
  @Autowired
  private CommentRepositiory commentRepositiory;

  final int pageCommentCount = 10; // 一页评论的数量

  @Autowired
  private UserService userService;

  public Page<CommentItemDTO> getVenueComments(Integer venueId, Integer page)
  {
    Pageable pageable = PageRequest.of(page, pageCommentCount, Sort.by("time").descending());
    return commentRepositiory.getVenueComments(venueId, pageable).map(comment -> new CommentItemDTO(
      comment.getCommentId(),
      comment.getContent(),
      comment.getTime(),
      comment.getVenueId(),
      comment.getScore(),
      comment.getUserId(),
      comment.getUserName(),
      userService.getUserPhoto(comment.getUserId())
    ));
  }

  public ResultMsg postVenueComment(PostCommentDTO comment, int userId)
  {
    VenueComment userComment = new VenueComment();
    BeanUtils.copyProperties(comment, userComment);
    userComment.setUserId(userId);
    commentRepositiory.save(userComment);
    return new ResultMsg("成功发布评论", 1);
  }
}
