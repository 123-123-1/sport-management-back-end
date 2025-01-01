package com.tongji.sportmanagement.VenueSubsystem.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.AccountSubsystem.Controller.UserController;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.VenueSubsystem.DTO.CommentItemDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.PostCommentDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.VenueCommentDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.VenueComment;
import com.tongji.sportmanagement.VenueSubsystem.Repository.CommentRepositiory;

@Service
public class CommentService
{
  @Autowired
  private CommentRepositiory commentRepositiory;

  final int pageCommentCount = 10; // 一页评论的数量

  @Autowired
  private UserController userController;

  public VenueCommentDTO getVenueComments(int venueId, long page)
  {
    // 1. 获取所有用户评论
    long offset = (page - 1) * pageCommentCount;
    List<VenueComment> comments = (List<VenueComment>)commentRepositiory.findCommentByVenueId(venueId, offset, pageCommentCount);
    // 2. 获取评论的所有用户信息
    List<CommentItemDTO> userComments = new ArrayList<CommentItemDTO>();
    for (VenueComment comment : comments) {
      userComments.add(new CommentItemDTO(comment, userController.getUserProfile(comment.getUserId())));
    }
    // 3. 生成查询结果
    VenueCommentDTO result = new VenueCommentDTO();
    result.setTotal(commentRepositiory.getCommentCount(venueId));
    result.setPage(page);
    result.setComments(userComments);
    return result;
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
