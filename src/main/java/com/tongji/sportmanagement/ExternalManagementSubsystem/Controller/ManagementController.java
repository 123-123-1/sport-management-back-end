package com.tongji.sportmanagement.ExternalManagementSubsystem.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ApiConfigCreateDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ApiConfigFieldsDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ApiConfigResponseDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.AvailabilityConfigInfoDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ChangeReservationStateDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ChangeReservationUserDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.CourtAvailabilityConfigDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationManagerMetaDTO;
// import com.tongji.sportmanagement.Common.DTO.VenueInitDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationStateCountResponseDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiConfig;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiType;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Service.ApiConfigService;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Service.AvailabilityConfigService;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Service.ManagementReservationService;
import com.tongji.sportmanagement.VenueSubsystem.DTO.CourtResponseDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.VenueDetailDTO;
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
  @Autowired
  private ApiConfigService apiConfigService;
  @Autowired
  private ManagementReservationService managementReservationService;

  // @PostMapping("/initialization")
  // public ResponseEntity<Object> initVenue(@RequestBody VenueInitDTO initInfo)
  // {
  //   return venueController.initVenue(initInfo);
  // }

  @GetMapping("/venueinfo")
  public ResponseEntity<VenueDetailDTO> getManagerVenue(@RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(venueService.getManagerVenue(idFromToken));
  }

  @PatchMapping("/venueinfo")
  public ResponseEntity<ResultMsg> patchVenue(@RequestBody Venue venueInfo, @RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(venueService.patchVenue(venueInfo, idFromToken));
  }

  @PostMapping("/venueimage")
  public ResponseEntity<ResultMsg> updateVenueImage(@RequestParam("image") MultipartFile image, @RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(venueService.updateVenueImage(image, idFromToken));
  }

  @PostMapping("/courts")
  public ResponseEntity<CourtResponseDTO> createCourt(@RequestBody Court courtInfo, @RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(courtService.createCourt(courtInfo, idFromToken));
  }

  @PatchMapping("/courts")
  public ResponseEntity<ResultMsg> patchCourt(@RequestBody Court courtInfo) throws Exception
  {
    return ResponseEntity.ok().body(courtService.patchCourt(courtInfo));
  }

  @DeleteMapping("/courts")
  public ResponseEntity<ResultMsg> deleteCourt(@RequestParam(required = false) Integer courtId,
  @RequestParam(required = false) String courtName, @RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(courtService.deleteCourt(courtId, courtName, idFromToken));
  }

  @PostMapping("/availability-config")
  public ResponseEntity<ResultMsg> createAvailabilityConfig(@RequestBody AvailabilityConfigInfoDTO configInfo)
  {
    return ResponseEntity.ok().body(availabilityConfigService.createAvailabilityConfig(configInfo));
  }

  @GetMapping("/availability-config")
  public ResponseEntity<List<CourtAvailabilityConfigDTO>> getAvailabilityConfig(@RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(availabilityConfigService.getAvailabilityConfig(idFromToken));
  }

  @PatchMapping("/availability-config")
  public ResponseEntity<ResultMsg> patchAvailabilityConfig(@RequestBody AvailabilityConfigInfoDTO configInfo) throws Exception
  {
    return ResponseEntity.ok().body(availabilityConfigService.patchAvailabilityConfig(configInfo));
  }

  @DeleteMapping("/availability-config")
  public ResponseEntity<ResultMsg> deleteAvailabilityConfig(@RequestParam Integer configId)
  {
    return ResponseEntity.ok().body(availabilityConfigService.deleteAvailabilityConfig(configId));
  }

  @GetMapping("/config-fields")
  public ResponseEntity<ApiConfigFieldsDTO> getConfigFields(@RequestParam ApiType type) throws Exception
  {
    return ResponseEntity.ok().body(apiConfigService.getConfigFields(type));
  }

  @GetMapping("/api-config")
  public ResponseEntity<ApiConfigResponseDTO> getApiConfig(@RequestParam ApiType type, @RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(apiConfigService.getConfigByManager(type, idFromToken));
  }

  @PostMapping("/api-config")
  public ResponseEntity<ApiConfig> createApiConfig(@RequestBody ApiConfigCreateDTO createInfo, @RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(apiConfigService.createApiConfig(createInfo, idFromToken));
  }

  @DeleteMapping("/api-config")
  public ResponseEntity<ResultMsg> deleteApiConfig(@RequestParam Integer apiConfigId)
  {
    return ResponseEntity.ok().body(apiConfigService.deleteApiConfig(apiConfigId));
  }

  @PatchMapping("/api-config")
  public ResponseEntity<ApiConfig> editApiConfig(@RequestBody ApiConfig config) throws Exception
  {
    return ResponseEntity.ok().body(apiConfigService.editApiConfig(config));
  }

  @GetMapping("/reservations/list")
  public ResponseEntity<Page<ReservationManagerMetaDTO>> getVenueReservations(@RequestAttribute Integer idFromToken, @RequestParam Integer page) throws Exception
  {
    return ResponseEntity.ok().body(managementReservationService.getVenueReservationByManager(idFromToken, null, null, page));
  }

  @PatchMapping("/reservations/user-state")
  public ResponseEntity<ResultMsg> changeUserReservationState(@RequestBody ChangeReservationUserDTO stateDto) throws Exception
  {
    return ResponseEntity.ok().body(managementReservationService.changeUserReservationState(stateDto));
  }

  @GetMapping("/reservations/state-count")
  public ResponseEntity<ReservationStateCountResponseDTO> getReservationStateCount(@RequestParam Integer reservationId) throws Exception
  {
    return ResponseEntity.ok().body(managementReservationService.countConflictReservation(reservationId));
  }

  @PatchMapping("/reservations/state")
  public ResponseEntity<ResultMsg> changeReservationState(@RequestBody ChangeReservationStateDTO stateDto) throws Exception
  {
    return ResponseEntity.ok().body(managementReservationService.changeReservationState(stateDto));
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

  // public ResponseEntity<ReservationResponseDTO> sendReservationRequest(ReservationRequestDTO requestDTO)
  // {
  //   // 暂时发回本地进行测试
  //   return restTemplate.postForEntity("http://localhost:8080/api/management/managermock", requestDTO, ReservationResponseDTO.class);
  // }

  // public ResponseEntity<ReservationResponseDTO> sendOccupyRequest(ReservationRequestDTO requestDTO)
  // {
  //   // 暂时发回本地进行测试
  //   return restTemplate.postForEntity("http://localhost:8080/api/management/managermock", requestDTO, ReservationResponseDTO.class);
  // }

  // @PostMapping("/managermock")
  // ResponseEntity<ReservationResponseDTO> managermock()
  // {
  //   return ResponseEntity.ok().body(new ReservationResponseDTO(1, "预约冲突"));
  // }

  public String sendReservationRequest(String reservationUrl, String reservationRequest)
  {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return restTemplate.postForObject(reservationUrl, new HttpEntity<>(reservationRequest, headers), String.class);
  }
}
