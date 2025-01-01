package com.tongji.sportmanagement.VenueSubsystem.Controller;

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
import com.tongji.sportmanagement.VenueSubsystem.DTO.PostCommentDTO;
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
  // public ResponseEntity<Object> postVenueComment(@RequestBody PostCommentDTO comment, @RequestAttribute int userId)
  // {
  //   try{
  //     return ResponseEntity.ok().body(commentService.postVenueComment(comment, userId));
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
}
