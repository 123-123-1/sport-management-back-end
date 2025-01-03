package com.tongji.sportmanagement.VenueSubsystem.Controller;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.DTO.ErrorMsg;
import com.tongji.sportmanagement.Common.DTO.VenueInitDTO;
import com.tongji.sportmanagement.Common.DTO.VenueInitResponseDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.CourtResponseDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.PostCommentDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Timeslot;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;
import com.tongji.sportmanagement.VenueSubsystem.Service.CommentService;
import com.tongji.sportmanagement.VenueSubsystem.Service.CourtService;
import com.tongji.sportmanagement.VenueSubsystem.Service.TimeslotService;
import com.tongji.sportmanagement.VenueSubsystem.Service.VenueService;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/venues")
public class VenueController 
{
  @Autowired
  private VenueService venueService;
  @Autowired
  private CourtService courtService;
  @Autowired
  private TimeslotService timeslotService;
  @Autowired
  private CommentService commentService;

  @GetMapping("/list")
  public ResponseEntity<Object> getVenueList(@RequestParam int page, @RequestParam String name)
  {
    try{
      return ResponseEntity.ok().body(venueService.getAllVenues(page, name));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @GetMapping("/detail")
  public ResponseEntity<Object> getVenueDetail(@RequestParam int venueId)
  {
    try{
      return ResponseEntity.ok().body(venueService.getVenueDetail(venueId));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @GetMapping("/courts")
  public ResponseEntity<Object> getVenueCourts(@RequestParam int venueId)
  {
    try{
      return ResponseEntity.ok().body(courtService.getVenueCourts(venueId));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @GetMapping("/timeslots")
  public ResponseEntity<Object> getVenueTimeslots(@RequestParam int venueId, @RequestParam String date)
  {
    try{
      return ResponseEntity.ok().body(timeslotService.getVenueTimeslots(venueId, date));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @GetMapping("/comments")
  public ResponseEntity<Object> getVenueComments(@RequestParam int venueId, @RequestParam long page)
  {
    try{
      return ResponseEntity.ok().body(commentService.getVenueComments(venueId, page));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @PostMapping("/comments")
  // 等待JWT相关功能
  // public ResponseEntity<Object> postVenueComment(@RequestBody PostCommentDTO comment, @RequestAttribute Integer idFromToken)
  // {
  //   try{
  //     return ResponseEntity.ok().body(commentService.postVenueComment(comment, idFromToken));
  //   }
  //   catch(Exception e){
  //     return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
  //   }
  // }
  public ResponseEntity<Object> postVenueComment(@RequestBody PostCommentDTO comment)
  {
    try{
      return ResponseEntity.ok().body(commentService.postVenueComment(comment, 1));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @GetMapping("/availabilities")
  public ResponseEntity<Object> getVenueAvailability(@RequestParam Integer availabilityId)
  {
    try{
      return ResponseEntity.ok().body(timeslotService.getAvailability(availabilityId));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  public ResponseEntity<Object> getAvalibilityByState(Integer timeslotId, String state)
  {
    try{
      return ResponseEntity.ok().body(timeslotService.getAvailabilityByState(timeslotId, state));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  // @GetMapping("/courts")
  public ResponseEntity<Object> getCourtById(Integer courtId)
  {
    try{
      return ResponseEntity.ok().body(courtService.getCourtById(courtId));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  public ResponseEntity<Object> getTimeslotById(Integer timeslotId)
  {
    try{
      return ResponseEntity.ok().body(timeslotService.getTimeslotById(timeslotId));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  // 以上为怡运动系统前后端接口
  // --------------------------------------------------------------------------------------
  // 以下为场地管理方对接怡运动系统接口

  public ResponseEntity<Object> initVenue(VenueInitDTO initInfo)
  {
    try{
      VenueInitResponseDTO result = venueService.createVenue(initInfo.getVenueInfo());
      List<CourtResponseDTO> courts = courtService.createCourts(initInfo.getCourts(), result.getVenueId());
      result.setCourts(courts);
      return ResponseEntity.ok().body(result);
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  public ResponseEntity<Object> patchVenue(Venue venueInfo, Integer venueId)
  {
    try{
      return ResponseEntity.ok().body(venueService.patchVenue(venueInfo, venueId));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  public ResponseEntity<Object> createCourt(Court courtInfo, Integer venueId)
  {
    try{
      return ResponseEntity.ok().body(courtService.createCourt(courtInfo, venueId));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  public ResponseEntity<Object> patchCourt(Court courtInfo, Integer venueId)
  {
    try{
      return ResponseEntity.ok().body(courtService.patchCourt(courtInfo, venueId));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  public ResponseEntity<Object> deleteCourt(Integer courtId, String courtName, Integer venueId)
  {
    try{
      return ResponseEntity.ok().body(courtService.deleteCourt(courtId, courtName, venueId));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  public ResponseEntity<Object> createTimeslot(Timeslot timeslotInfo, Integer venueId)
  {
    try{
      return ResponseEntity.ok().body(timeslotService.createTimeslot(timeslotInfo, venueId));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  public ResponseEntity<Object> deleteTimeslot(Integer timeslotId, Instant startTime, Instant endTime, Integer venueId)
  {
    try{
      return ResponseEntity.ok().body(timeslotService.deleteTimeslot(timeslotId, startTime, endTime, venueId));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  public ResponseEntity<Object> createAvailability(CourtAvailability availability)
  {
    try{
      return ResponseEntity.ok().body(timeslotService.createAvailability(availability));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  public ResponseEntity<Object> patchAvailability(CourtAvailability availability)
  {
    try{
      return ResponseEntity.ok().body(timeslotService.patchAvailability(availability));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  public ResponseEntity<Object> deleteAvailability(Integer availabilityId, Integer courtId, Integer timeslotId)
  {
    try{
      return ResponseEntity.ok().body(timeslotService.deleteAvailability(availabilityId, courtId, timeslotId));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }
}
