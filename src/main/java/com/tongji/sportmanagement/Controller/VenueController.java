package com.tongji.sportmanagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import com.tongji.sportmanagement.DTO.VenueListDTO;
import com.tongji.sportmanagement.Entity.Venue;
import com.tongji.sportmanagement.Repository.VenueRepository;

import java.util.List;

import com.tongji.sportmanagement.DTO.ErrorMsg;
import com.tongji.sportmanagement.Service.VenueService;

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

  // @GetMapping("/timeslots")
  // public ArrayList<Timeslot> getVenueTimeslots(@RequestParam String date)
  // {

  // }

  // @GetMapping("/comments")
  // public ArrayList<Comment> getVenueComments(@RequestParam int venueID)
  // {

  // }

  // @PostMapping("/comments")
  // public ResultMsg postVenueComment(@RequestBody Comment comment)
  // {

  // }
}
