package com.tongji.sportmanagement.VenueSubsystem.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tongji.sportmanagement.VenueSubsystem.Repository.CourtAvailabilityRepository;
import com.tongji.sportmanagement.VenueSubsystem.Repository.CourtRepository;
import com.tongji.sportmanagement.VenueSubsystem.Repository.TimeslotRepository;
import com.tongji.sportmanagement.VenueSubsystem.Repository.VenueRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.AccountSubsystem.Controller.UserController;
import com.tongji.sportmanagement.Common.DTO.ErrorMsg;
import com.tongji.sportmanagement.VenueSubsystem.DTO.CommentItemDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.PostCommentDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.VenueCommentDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.VenueListDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.VenueTimeslotDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Timeslot;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;
import com.tongji.sportmanagement.VenueSubsystem.Entity.VenueComment;
import com.tongji.sportmanagement.VenueSubsystem.Repository.CommentRepositiory;

@Service
public class VenueService
{
  @Autowired
  private VenueRepository venueRepository;
  @Autowired
  private CourtRepository courtRepository;
  @Autowired
  private TimeslotRepository timeslotRepository;
  @Autowired
  private CourtAvailabilityRepository courtAvailabilityRepository;
  @Autowired
  private CommentRepositiory commentRepositiory;

  @Autowired
  private UserController userController;

  final int pageVenueCount = 10; // 一页场馆的数量
  final int pageCommentCount = 10; // 一页评论的数量

  // 获取所有场馆 or 根据名称关键字查找场馆
  public ResponseEntity<Object> getAllVenues(int page, String name)
  {
    List<Venue> result;
    long total = 0;
    if(name.isBlank()){
      result = (List<Venue>) venueRepository.findPageVenue((page - 1) * pageVenueCount, pageVenueCount);
      total = venueRepository.count();
    }
    else{
      result = (List<Venue>) venueRepository.findVenueByName(name, (page - 1) * pageVenueCount, pageVenueCount);
      total = venueRepository.getVenueNameCount(name);
    }
    return ResponseEntity.ok().body(new VenueListDTO(total, page, result));
  }

  // 根据场馆ID获取场馆详细信息
  public ResponseEntity<Object> getVenueDetail(int venueId)
  {
    Optional<Venue> result = venueRepository.findById(venueId);
    if(result.isPresent()){
      return ResponseEntity.ok().body(result.get());
    }
    else{
      return ResponseEntity.status(404).body(new ErrorMsg("场地不存在"));
    }
  }

  // 根据场馆ID获取场馆的所有场地
  public ResponseEntity<Object> getVenueCourts(int venueId)
  {
    return ResponseEntity.ok().body((List<Court>)courtRepository.findAllByVenueId(venueId));
  }

  // 获取场馆的所有Timeslot及其开放信息
  public ResponseEntity<Object> getVenueTimeslots(int venueId, String date)
  {
    // 1. 获取场馆的所有时间段
    Instant start_date = Instant.parse(date + "T00:00:00Z");
    Instant end_date = start_date.atZone(ZoneId.systemDefault()).plusDays(1).toInstant();
    List<Timeslot> timeslots = (List<Timeslot>)timeslotRepository.findByDate(start_date, end_date);
    // 2. 获取时间段的所有可预约项
    List<VenueTimeslotDTO> result = new ArrayList<VenueTimeslotDTO>();
    for (Timeslot timeslot : timeslots) {
      List<CourtAvailability> availabilities = (List<CourtAvailability>)courtAvailabilityRepository.findAllByTimeslotId(timeslot.getTimeslotId());
      result.add(new VenueTimeslotDTO(timeslot, availabilities));
    }
    return ResponseEntity.ok().body(result);
  }

  public ResponseEntity<Object> getVenueComments(int venueId, long page)
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
    return ResponseEntity.ok().body(result);
  }

  public ResponseEntity<Object> postVenueComment(PostCommentDTO comment, int userId)
  {
    VenueComment userComment = new VenueComment();
    BeanUtils.copyProperties(comment, userComment);
    userComment.setUserId(userId);
    commentRepositiory.save(userComment);
    return ResponseEntity.ok().body("success");
  }
}

