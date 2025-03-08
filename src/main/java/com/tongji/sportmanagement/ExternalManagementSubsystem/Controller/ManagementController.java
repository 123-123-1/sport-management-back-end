package com.tongji.sportmanagement.ExternalManagementSubsystem.Controller;

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
import org.springframework.web.multipart.MultipartFile;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.DTO.ErrorMsg;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.AvailabilityConfigInfoDTO;
// import com.tongji.sportmanagement.Common.DTO.VenueInitDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationRequestDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationResponseDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Service.AvailabilityConfigService;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;
import com.tongji.sportmanagement.VenueSubsystem.Service.CourtService;
import com.tongji.sportmanagement.VenueSubsystem.Service.VenueService;

@RestController
@RequestMapping("/api/management")
public class ManagementController
{
  @Autowired
  private VenueService venueService;
  @Autowired
  private RestTemplate restTemplate; // 用于向场地管理方发送预约请求
  @Autowired
  private CourtService courtService;
  @Autowired
  private AvailabilityConfigService availabilityConfigService;

  // @PostMapping("/initialization")
  // public ResponseEntity<Object> initVenue(@RequestBody VenueInitDTO initInfo)
  // {
  //   return venueController.initVenue(initInfo);
  // }

  @GetMapping("/venueinfo")
  public ResponseEntity<Object> getManagerVenue(@RequestAttribute Integer idFromToken)
  {
    try{
      return ResponseEntity.ok().body(venueService.getManagerVenue(idFromToken));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @PatchMapping("/venueinfo")
  public ResponseEntity<Object> patchVenue(@RequestBody Venue venueInfo, @RequestAttribute Integer idFromToken)
  {
    try{
      return ResponseEntity.ok().body(venueService.patchVenue(venueInfo, idFromToken));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @PostMapping("/venueimage")
  public ResponseEntity<Object> updateVenueImage(@RequestParam("image") MultipartFile image, @RequestAttribute Integer idFromToken)
  {
    try{
      return ResponseEntity.ok().body(venueService.updateVenueImage(image, idFromToken));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @PostMapping("/courts")
  public ResponseEntity<Object> createCourt(@RequestBody Court courtInfo, @RequestAttribute Integer idFromToken)
  {
    try{
      return ResponseEntity.ok().body(courtService.createCourt(courtInfo, idFromToken));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @PatchMapping("/courts")
  public ResponseEntity<Object> patchCourt(@RequestBody Court courtInfo)
  {
    try{
      return ResponseEntity.ok().body(courtService.patchCourt(courtInfo));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @DeleteMapping("/courts")
  public ResponseEntity<Object> deleteCourt(@RequestParam(required = false) Integer courtId,
  @RequestParam(required = false) String courtName, @RequestAttribute Integer idFromToken)
  {
    try{
      return ResponseEntity.ok().body(courtService.deleteCourt(courtId, courtName, idFromToken));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @PostMapping("/availability-config")
  public ResponseEntity<Object> createAvailabilityConfig(@RequestBody AvailabilityConfigInfoDTO configInfo)
  {
    try{
      return ResponseEntity.ok().body(availabilityConfigService.createAvailabilityConfig(configInfo));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @GetMapping("/availability-config")
  public ResponseEntity<Object> getAvailabilityConfig(@RequestAttribute Integer idFromToken)
  {
    try{
      return ResponseEntity.ok().body(availabilityConfigService.getAvailabilityConfig(idFromToken));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @PatchMapping("/availability-config")
  public ResponseEntity<Object> patchAvailabilityConfig(@RequestBody AvailabilityConfigInfoDTO configInfo)
  {
    try{
      return ResponseEntity.ok().body(availabilityConfigService.patchAvailabilityConfig(configInfo));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  // @PostMapping("/timeslots")
  // 等待JWT
  // public ResponseEntity<Object> createTimeslot(@RequestBody Timeslot timeslotInfo, @RequestAttribute Integer idFromToken)
  // {
  //   return venueController.createTimeslot(timeslotInfo, idFromToken);
  // }
  // public ResponseEntity<Object> createTimeslot(@RequestBody Timeslot timeslotInfo)
  // {
  //   return venueController.createTimeslot(timeslotInfo, 1);
  // }

  // @DeleteMapping("/timeslots")
  // // 等待JWT
  // public ResponseEntity<Object> deleteTimeslot(@RequestParam(required = false) Integer timeslotId, 
  // @RequestParam(required = false) Instant startTime, @RequestParam(required = false) Instant endTime,
  // @RequestAttribute Integer venueId){
  //   return venueController.deleteTimeslot(timeslotId, startTime, endTime, venueId);
  // }
  // public ResponseEntity<Object> deleteTimeslot(@RequestParam(required = false) Integer timeslotId, 
  // @RequestParam(required = false) Instant startTime, @RequestParam(required = false) Instant endTime){
  //   return venueController.deleteTimeslot(timeslotId, startTime, endTime, 14);
  // }

  // @PostMapping("/availabilities")
  // public ResponseEntity<Object> createAvailability(@RequestBody CourtAvailability availability)
  // {
  //   return venueController.createAvailability(availability);
  // }

  // @PatchMapping("/availabilities")
  // public ResponseEntity<Object> patchAvailability(@RequestBody CourtAvailability availability)
  // {
  //   return venueController.patchAvailability(availability);
  // }

  // @DeleteMapping("/availabilities")
  // public ResponseEntity<Object> deleteAvailability(@RequestParam(required = false) Integer availabilityId, 
  // @RequestParam(required = false) Integer courtId, @RequestParam(required = false) Integer timeslotId){
  //   return venueController.deleteAvailability(availabilityId, courtId, timeslotId);
  // }

  // @GetMapping("/courts")
  // public ResponseEntity<Object> getCourtInfo(@RequestAttribute Integer idFromToken){
  //   return venueController.getVenueCourts(idFromToken);
  // }

  // public ResponseEntity<Object> getTimeslotInfo(@RequestAttribute Integer idFromToken, String date){
  //   return venueController.getVenueTimeslots(idFromToken, date);
  // }

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
