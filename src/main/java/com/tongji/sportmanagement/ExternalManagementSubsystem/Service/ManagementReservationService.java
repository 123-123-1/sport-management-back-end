package com.tongji.sportmanagement.ExternalManagementSubsystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationManagerMetaDTO;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.Reservation;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.ReservationRepository;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.ReservationSpecification;

@Service
public class ManagementReservationService
{
  @Autowired
  ManagementUtilsService managementUtilsService;
  @Autowired
  private ReservationRepository reservationRepository;

  final static int ReservationPageCount = 10;

  // 场地管理方获取预约信息
  public Page<ReservationManagerMetaDTO> getVenueReservationByManager(Integer managerId, Integer userId, String userName, Integer page) throws Exception
  {
    Integer venueId = managementUtilsService.getVenueIdByManager(managerId);
    if (userId != null && userName != null) {
        throw new ServiceException(422, "userId和userName不能同时传入");
    }

    Specification<Reservation> spec = ReservationSpecification.filterByUser(userId, userName);
    Pageable pageable = PageRequest.of(page, ReservationPageCount);
    return reservationRepository.getReservationByVenue(venueId, spec, pageable);
  }

    
}
