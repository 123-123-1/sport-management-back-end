package com.tongji.sportmanagement.ExternalManagementSubsystem.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ChangeReservationStateDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ChangeReservationUserDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationManagerMetaDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationStateCountDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationStateCountResponseDTO;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.Reservation;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationOperation;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationRecord;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationState;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationUserState;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.UserReservation;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.ReservationRecordRepository;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.ReservationRepository;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.ReservationSpecification;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.UserReservationRepository;
import com.tongji.sportmanagement.ReservationSubsystem.Service.ViolationService;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailabilityState;
import com.tongji.sportmanagement.VenueSubsystem.Service.TimeslotService;

@Service
public class ManagementReservationService
{
  @Autowired
  ManagementUtilsService managementUtilsService;
  @Autowired
  TimeslotService timeslotService;
  @Autowired
  ViolationService violationService;
  @Autowired
  private ReservationRepository reservationRepository;
  @Autowired
  private UserReservationRepository userReservationRepository;
  @Autowired
  private ReservationRecordRepository reservationRecordRepository;

  final static int ReservationPageCount = 20;

  // 场地管理方获取预约信息
  public Page<ReservationManagerMetaDTO> getVenueReservationByManager(Integer managerId, Integer userId, String userName, Integer page) throws Exception
  {
    Integer venueId = managementUtilsService.getVenueIdByManager(managerId);
    if (userId != null && userName != null) {
      throw new ServiceException(422, "userId和userName不能同时传入");
    }

    Specification<Reservation> spec = ReservationSpecification.filterByUser(userId, userName);
    Pageable pageable = PageRequest.of(page, ReservationPageCount, Sort.by("reservationId").descending());
    return reservationRepository.getReservationByVenue(venueId, spec, pageable);
  }

  // 修改用户预约状态
  public ResultMsg changeUserReservationState(ChangeReservationUserDTO stateDto) throws Exception
  {
    // 1. 更新用户预约状态
    Optional<UserReservation> userReservationOptional = userReservationRepository.findByUserIdAndReservationId(stateDto.getReservationId(), stateDto.getUserId());
    if(userReservationOptional.isEmpty()){
      throw new ServiceException(404, "未找到要更改的用户信息");
    }
    UserReservation userReservation = userReservationOptional.get();
    userReservation.setUserState(stateDto.getUserState());
    userReservationRepository.save(userReservation);

    // 2. 违约处理
    if(stateDto.getUserState() == ReservationUserState.violated){
      violationService.violationIncrement(stateDto.getUserId());
    }

    // 3. 更新预约记录
    ReservationOperation operation = ReservationOperation.getManagerOperationByUserState(stateDto.getUserState());
    ReservationRecord record = new ReservationRecord(null, operation, Instant.now(), stateDto.getUserId(), stateDto.getReservationId());
    reservationRecordRepository.save(record);
    return ResultMsg.success("更新用户状态成功");
  }

  public ReservationStateCountResponseDTO countConflictReservation(Integer reservationId) throws Exception
  {
    Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
    if(reservationOptional.isEmpty()){
      throw new ServiceException(404, "未找到预约项");
    }
    Integer availabilityId = reservationOptional.get().getAvailabilityId();
    List<ReservationStateCountDTO> stateCount = reservationRepository.countConflictReservation(availabilityId);
    ReservationStateCountResponseDTO result = new ReservationStateCountResponseDTO(0L, 0L);
    for (ReservationStateCountDTO stateCountItem : stateCount) {
      if(stateCountItem.getState() == ReservationState.normal || stateCountItem.getState() == ReservationState.matching){
        result.setNormalCount(result.getNormalCount() + stateCountItem.getCount());
      }
      else if(stateCountItem.getState() == ReservationState.pending){
        result.setPendingCount(result.getPendingCount() + stateCountItem.getCount());
      }
    }
    result.setPendingCount(result.getPendingCount() - 1);
    return result;
  }

  // 修改预约状态
  public ResultMsg changeReservationState(ChangeReservationStateDTO stateDto) throws Exception
  {
    // 1. 更新预约状态
    Optional<Reservation> reservationOptional = reservationRepository.findById(stateDto.getReservationId());
    if(reservationOptional.isEmpty()){
      throw new ServiceException(404, "未找到预约信息");
    }
    Reservation reservation = reservationOptional.get();
    reservation.setState(stateDto.getState());
    reservationRepository.save(reservation);

    // 2. 更新场地状态信息
    if(stateDto.getChangeAvailability()){
      CourtAvailability courtAvailability = reservationRepository.getReservationCourtAvailability(stateDto.getReservationId());
      timeslotService.changeAvailabilityState(courtAvailability, CourtAvailabilityState.reserveable);
    }

    // 3. 写入预约记录
    ReservationOperation operation = ReservationOperation.getManagerOperationByReservationState(stateDto.getState());
    ReservationRecord record = new ReservationRecord(null, operation, Instant.now(), null, stateDto.getReservationId());
    reservationRecordRepository.save(record);
    return ResultMsg.success("修改预约状态成功");
  }
}
