package com.tongji.sportmanagement.ReservationSubsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.CancelReservationDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.GroupRequestDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.IndividualRequestDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.MatchRequestDTO;
import com.tongji.sportmanagement.ReservationSubsystem.Service.ReservationService;
import com.tongji.sportmanagement.ReservationSubsystem.Service.ViolationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController
{
  @Autowired
  private ReservationService reservationService;

  @Autowired
  private ViolationService violationService;

  @PostMapping("/individual")
  ResponseEntity<Object> individualReservation(@RequestBody IndividualRequestDTO reservationInfo, @RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(reservationService.individualReservation(reservationInfo, idFromToken));
  }

  @PostMapping("/group")
  ResponseEntity<Object> groupReservation(@RequestBody GroupRequestDTO reservationInfo, @RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(reservationService.groupReservation(reservationInfo, idFromToken));
  }

  @PostMapping("/match")
  ResponseEntity<Object> matchReservation(@RequestBody MatchRequestDTO reservationInfo, @RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(reservationService.matchReservation(reservationInfo, idFromToken));
  }

  @GetMapping("/list")
  ResponseEntity<Object> getReservationList(@RequestParam Integer page, @RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(reservationService.getUserReservations(idFromToken, page));
  }

  @GetMapping("/detail")
  ResponseEntity<Object> getReservationDetail(@RequestParam Integer reservationId, @RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(reservationService.getReservationDetail(reservationId, idFromToken));
  }

  @GetMapping("/violations")
  ResponseEntity<Object> getUserReservation(@RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(violationService.getUserViolation(idFromToken));
  }

  @PatchMapping("/cancel")
  ResponseEntity<ResultMsg> userCancelReservation(@RequestBody CancelReservationDTO cancelDto, @RequestAttribute Integer idFromToken) throws Exception
  {
    return ResponseEntity.ok().body(reservationService.userCancelReservation(cancelDto, idFromToken));
  }
}
