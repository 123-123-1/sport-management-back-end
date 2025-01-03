package com.tongji.sportmanagement.ExternalManagementSubsystem.Controller;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.tongji.sportmanagement.Common.DTO.VenueInitDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationRequestDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationResponseDTO;
import com.tongji.sportmanagement.VenueSubsystem.Controller.VenueController;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Timeslot;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;

@RestController
@RequestMapping("/api/management")
public class ManagementController
{
  @Autowired
  private VenueController venueController;
  @Autowired
  private RestTemplate restTemplate; // 用于向场地管理方发送预约请求

  @PostMapping("/initialization")
  public ResponseEntity<Object> initVenue(@RequestBody VenueInitDTO initInfo)
  {
    return venueController.initVenue(initInfo);
  }

  @GetMapping("/venueinfo")
  // 等待JWT功能
  // public ResponseEntity<Object> getVenueInfo(@RequestAttribute Integer idFromToken)
  // {
  //   return venueController.getVenueDetail(idFromToken);
  // }
  public ResponseEntity<Object> getVenueInfo()
  {
    return venueController.getVenueDetail(14);
  }

  @PatchMapping("/venueinfo")
  // 等待JWT功能
  // public ResponseEntity<Object> patchVenueInfo(@RequestBody Venue venueInfo, @RequestAttribute Integer idFromToken)
  // {
  //   return venueController.patchVenue(venueInfo, idFromToken);
  // }
  public ResponseEntity<Object> patchVenueInfo(@RequestBody Venue venueInfo)
  {
    return venueController.patchVenue(venueInfo, 14);
  }

  @PostMapping("/courts")
  // 等待JWT功能
  // public ResponseEntity<Object> createCourt(@RequestBody Court courtInfo, @RequestAttribute Integer idFromToken)
  // {
  //   return venueController.createCourt(courtInfo, idFromToken);
  // }
  public ResponseEntity<Object> createCourt(@RequestBody Court courtInfo)
  {
    return venueController.createCourt(courtInfo, 14);
  }

  @PatchMapping("/courts")
  // 等待JWT功能
  // public ResponseEntity<Object> patchCourt(@RequestBody Court courtInfo, @RequestAttribute Integer idFromToken)
  // {
  //   return venueController.patchCourt(courtInfo, idFromToken);
  // }
  public ResponseEntity<Object> patchCourt(@RequestBody Court courtInfo)
  {
    return venueController.patchCourt(courtInfo, 14);
  }

  @DeleteMapping("/courts")
  // 等待JWT功能
  // public ResponseEntity<Object> deleteCourt(@RequestParam(required = false) Integer courtId,
  // @RequestParam(required = false) String courtName, @RequestAttribute Integer idFromToken)
  // {
  //   return venueController.deleteCourt(courtId, courtName, idFromToken);
  // }
  public ResponseEntity<Object> deleteCourt(@RequestParam(required = false) Integer courtId,
  @RequestParam(required = false) String courtName)
  {
    return venueController.deleteCourt(courtId, courtName, 14);
  }

  @PostMapping("/timeslots")
  // 等待JWT
  // public ResponseEntity<Object> createTimeslot(@RequestBody Timeslot timeslotInfo, @RequestAttribute Integer idFromToken)
  // {
  //   return venueController.createTimeslot(timeslotInfo, idFromToken);
  // }
  public ResponseEntity<Object> createTimeslot(@RequestBody Timeslot timeslotInfo)
  {
    return venueController.createTimeslot(timeslotInfo, 1);
  }

  @DeleteMapping("/timeslots")
  // 等待JWT
  // public ResponseEntity<Object> deleteTimeslot(@RequestParam(required = false) Integer timeslotId, 
  // @RequestParam(required = false) Instant startTime, @RequestParam(required = false) Instant endTime,
  // @RequestAttribute Integer venueId){
  //   return venueController.deleteTimeslot(timeslotId, startTime, endTime, venueId);
  // }
  public ResponseEntity<Object> deleteTimeslot(@RequestParam(required = false) Integer timeslotId, 
  @RequestParam(required = false) Instant startTime, @RequestParam(required = false) Instant endTime){
    return venueController.deleteTimeslot(timeslotId, startTime, endTime, 14);
  }

  @PostMapping("/availabilities")
  public ResponseEntity<Object> createAvailability(@RequestBody CourtAvailability availability)
  {
    return venueController.createAvailability(availability);
  }

  @PatchMapping("/availabilities")
  public ResponseEntity<Object> patchAvailability(@RequestBody CourtAvailability availability)
  {
    return venueController.patchAvailability(availability);
  }

  @DeleteMapping("/availabilities")
  public ResponseEntity<Object> deleteAvailability(@RequestParam(required = false) Integer availabilityId, 
  @RequestParam(required = false) Integer courtId, @RequestParam(required = false) Integer timeslotId){
    return venueController.deleteAvailability(availabilityId, courtId, timeslotId);
  }

  @GetMapping("/courts")
  public ResponseEntity<Object> getCourtInfo(@RequestAttribute Integer idFromToken){
    return venueController.getVenueCourts(idFromToken);
  }

  public ResponseEntity<Object> getTimeslotInfo(@RequestAttribute Integer idFromToken, String date){
    return venueController.getVenueTimeslots(idFromToken, date);
  }

  public ResponseEntity<ReservationResponseDTO> sendReservationRequest(ReservationRequestDTO requestDTO)
  {
    // 暂时发回本地进行测试
    return restTemplate.postForEntity("http://localhost:8080/api/management/managermock", requestDTO, ReservationResponseDTO.class);
  }

  public ResponseEntity<ReservationResponseDTO> sendOccupyRequest(ReservationRequestDTO requestDTO)
  {
    // 暂时发回本地进行测试
    return restTemplate.postForEntity("http://localhost:8080/api/management/managermock", requestDTO, ReservationResponseDTO.class);
  }

  @PostMapping("/managermock")
  ResponseEntity<ReservationResponseDTO> managermock()
  {
    return ResponseEntity.ok().body(new ReservationResponseDTO(1, "预约冲突"));
  }
}
