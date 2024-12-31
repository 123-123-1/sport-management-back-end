package com.tongji.sportmanagement.VenueSubsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongji.sportmanagement.AccountSubsystem.Controller.UserController;
import com.tongji.sportmanagement.VenueSubsystem.Service.VenueService;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/venues")
public class VenueController 
{
  @Autowired
  private VenueService service;

  @Autowired
  private UserController userController;

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

  // @GetMapping("/comments")
  // public ResponseEntity<Object> getVenueComments(@RequestParam int venueID)
  // {

  // }

  // @PostMapping("/comments")
  // public ResultMsg postVenueComment(@RequestBody Comment comment)
  // {

  // }
}
