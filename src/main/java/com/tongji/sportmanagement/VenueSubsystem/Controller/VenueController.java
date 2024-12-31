package com.tongji.sportmanagement.VenueSubsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongji.sportmanagement.VenueSubsystem.DTO.PostCommentDTO;
import com.tongji.sportmanagement.VenueSubsystem.Service.VenueService;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/venues")
public class VenueController 
{
  @Autowired
  private VenueService service;

  @GetMapping("/list")
  public ResponseEntity<Object> getVenueList(@RequestParam int page, @RequestParam String name)
  {
    return service.getAllVenues(page, name);
  }

  @GetMapping("/detail")
  public ResponseEntity<Object> getVenueDetail(@RequestParam int venueId)
  {
    return service.getVenueDetail(venueId);
  }

  @GetMapping("/courts")
  public ResponseEntity<Object> getVenueCourts(@RequestParam int venueId)
  {
    return service.getVenueCourts(venueId);
  }

  @GetMapping("/timeslots")
  public ResponseEntity<Object> getVenueTimeslots(@RequestParam int venueId, @RequestParam String date)
  {
    return service.getVenueTimeslots(venueId, date);
  }

  @GetMapping("/comments")
  public ResponseEntity<Object> getVenueComments(@RequestParam int venueId, @RequestParam long page)
  {
    return service.getVenueComments(venueId, page);
  }

  @PostMapping("/comments")
  // 等待JWT相关功能
  // public ResponseEntity<Object> postVenueComment(@RequestBody PostCommentDTO comment, @RequestAttribute int userId)
  // {
  //   return service.postVenueComment(comment, userId);
  // }
  public ResponseEntity<Object> postVenueComment(@RequestBody PostCommentDTO comment)
  {
    return service.postVenueComment(comment, 1);
  }
}
