package com.tongji.sportmanagement.ReservationSubsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.DTO.ErrorMsg;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationResponseDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.GroupRequestDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.IndividualRequestDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.MatchRequestDTO;
import com.tongji.sportmanagement.ReservationSubsystem.Service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController
{
  @Autowired
  private ReservationService reservationService;

  @PostMapping("/individual")
  ResponseEntity<Object> individualReservation(@RequestBody IndividualRequestDTO reservationInfo, @RequestAttribute Integer idFromToken)
  {
    try{
      return ResponseEntity.ok().body(reservationService.individualReservation(reservationInfo, idFromToken));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }
  // @PostMapping("/individual")
  // ResponseEntity<Object> individualReservation(@RequestBody IndividualRequestDTO reservationInfo)
  // {
  //   try{
  //     return ResponseEntity.ok().body(reservationService.individualReservation(reservationInfo, 1));
  //   }
  //   catch(ServiceException e){
  //     return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
  //   }
  //   catch(Exception e){
  //     return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
  //   }
  // }

  @PostMapping("/group")
  // 等待JWT功能
  ResponseEntity<Object> groupReservation(@RequestBody GroupRequestDTO reservationInfo, @RequestAttribute Integer idFromToken)
  {
    try{
      return ResponseEntity.ok().body(reservationService.groupReservation(reservationInfo, idFromToken));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }
  // ResponseEntity<Object> groupReservation(@RequestBody GroupRequestDTO reservationInfo)
  // {
  //   try{
  //     return ResponseEntity.ok().body(reservationService.groupReservation(reservationInfo, 1));
  //   }
  //   catch(ServiceException e){
  //     return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
  //   }
  //   catch(Exception e){
  //     return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
  //   }
  // }

  @PostMapping("/match")
  ResponseEntity<Object> matchReservation(@RequestBody MatchRequestDTO reservationInfo)
  {
    try{
      return ResponseEntity.ok().body(reservationService.matchReservation(reservationInfo));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @GetMapping("/list")
  ResponseEntity<Object> getReservationList(@RequestAttribute Integer idFromToken)
  {
    try{
      return ResponseEntity.ok().body(reservationService.getUserReservations(idFromToken));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }

  @GetMapping("/detail")
  ResponseEntity<Object> getReservationDetail(@RequestParam Integer reservationId, @RequestAttribute Integer idFromToken)
  {
    try{
      return ResponseEntity.ok().body(reservationService.getReservationDetail(reservationId, idFromToken));
    }
    catch(ServiceException e){
      return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
    }
  }
}
