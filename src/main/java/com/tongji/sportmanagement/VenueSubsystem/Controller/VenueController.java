package com.tongji.sportmanagement.VenueSubsystem.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongji.sportmanagement.VenueSubsystem.DTO.CourtDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.PostCommentDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.VenueDetailDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.VenueTimeslotDTO;
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
  public ResponseEntity<Page<VenueDetailDTO>> getVenueList(@RequestParam int page, @RequestParam String name)
  {
    return ResponseEntity.ok().body(venueService.getAllVenues(page, name));
  }

  @GetMapping("/detail")
  public ResponseEntity<VenueDetailDTO> getVenueDetail(@RequestParam int venueId) throws Exception
  {
    return ResponseEntity.ok().body(venueService.getVenueDetail(venueId));
  }

  @GetMapping("/courts")
  public ResponseEntity<List<CourtDTO>> getVenueCourts(@RequestParam int venueId)
  {
    return ResponseEntity.ok().body(courtService.getVenueCourts(venueId));
  }

  @GetMapping("/court-type")
  public ResponseEntity<List<String>> getVenueCourtType(@RequestParam int venueId)
  {
    return ResponseEntity.ok().body(courtService.getVenueCourtType(venueId));
  }

  @GetMapping("/timeslots")
  public ResponseEntity<List<VenueTimeslotDTO>> getVenueTimeslots(@RequestParam int venueId, @RequestParam String date)
  {
    return ResponseEntity.ok().body(timeslotService.getVenueTimeslots(venueId, date));
  }

  @GetMapping("/comments")
  public ResponseEntity<Object> getVenueComments(@RequestParam Integer venueId, @RequestParam Integer page)
  {
    return ResponseEntity.ok().body(commentService.getVenueComments(venueId, page));
  }

  @PostMapping("/comments")
  public ResponseEntity<Object> postVenueComment(@RequestBody PostCommentDTO comment, @RequestAttribute Integer idFromToken)
  {
    return ResponseEntity.ok().body(commentService.postVenueComment(comment, idFromToken));
  }

  @GetMapping("/availabilities")
  public ResponseEntity<Object> getVenueAvailability(@RequestParam Integer availabilityId) throws Exception
  {
    return ResponseEntity.ok().body(timeslotService.getAvailability(availabilityId));
  }
}
