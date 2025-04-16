package com.tongji.sportmanagement.VenueSubsystem.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.SportManagementUtils;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.VenueSubsystem.DTO.CourtDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.CourtResponseDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;
import com.tongji.sportmanagement.VenueSubsystem.Repository.CourtAvailabilityRepository;
import com.tongji.sportmanagement.VenueSubsystem.Repository.CourtRepository;
import com.tongji.sportmanagement.VenueSubsystem.Repository.VenueRepository;

@Service
public class CourtService
{
  @Autowired
  private CourtRepository courtRepository;
  @Autowired
  private VenueRepository venueRepository;
  @Autowired
  private CourtAvailabilityRepository courtAvailabilityRepository;

  // 根据场馆ID获取场馆的所有场地
  public List<CourtDTO> getVenueCourts(int venueId)
  {
    return courtRepository.findAllByVenueId(venueId);
  }

  // 批量创建场馆的场地
  public List<CourtResponseDTO> createCourts(List<Court> courts, int venueId)
  {
    for (Court court : courts) {
      court.setVenueId(venueId);
    }
    courtRepository.saveAll(courts);
    List<CourtResponseDTO> result = courts.stream().map(court -> new CourtResponseDTO(court.getCourtId(), court.getCourtName())).toList();
    return result;
  }

  public CourtResponseDTO createCourt(Court courtInfo, Integer managerId) throws Exception
  {
    Optional<Venue> targetVenue = venueRepository.findByManagerId(managerId);
    if(targetVenue.isEmpty()){
      throw new ServiceException(404, "未找到管理的场地");
    }
    courtInfo.setVenueId(targetVenue.get().getVenueId());
    courtRepository.save(courtInfo);
    return new CourtResponseDTO(courtInfo.getCourtId(), courtInfo.getCourtName());
  }

  public ResultMsg patchCourt(Court courtInfo) throws Exception
  {
    courtInfo.setVenueId(null);
    Optional<Court> court = courtRepository.findById(courtInfo.getCourtId());
    if(!court.isPresent()){
      throw new ServiceException(404, "未找到对应场地");
    }
    Court editedCourt = court.get();
    SportManagementUtils.copyNotNullProperties(courtInfo, editedCourt);
    courtRepository.save(editedCourt);
    return new ResultMsg("已成功编辑场地信息", 1);
  }

  public ResultMsg deleteCourt(Integer courtId, String courtName, Integer managerId) throws Exception
  {
    Integer targetId = courtId;
    if(targetId == null){
      if(courtName == null || managerId == null){
        throw new ServiceException(422, "场地名称和管理员ID不能为空");
      }
      Optional<Venue> targetVenue = venueRepository.findByManagerId(managerId);
      if(targetVenue.isEmpty()){
        throw new ServiceException(404, "未找到用户管理的场地");
      }
      Optional<Integer> optionalId = courtRepository.findVenueCourt(courtName, targetVenue.get().getVenueId());
      if(optionalId.isEmpty()){
        throw new ServiceException(404, "未找到对应场地");
      }
      targetId = optionalId.get();
    }
    if(courtAvailabilityRepository.hasCourtAvailability(targetId) != 0){
      throw new ServiceException(422, "删除场地失败：该场地有关联的开放时间段");
    }
    courtRepository.deleteById(targetId);
    return new ResultMsg("已成功删除场地", 1);
  }

  public Court getCourtById(Integer courtId) throws Exception
  {
    Optional<Court> result = courtRepository.findById(courtId);
    if(result.isEmpty()){
      throw new ServiceException(404, "未找到场地");
    }
    return result.get();
  }

  public List<Integer> findCourtByType(Integer venueId, String type)
  {
    return courtRepository.findAllByVenueIdAndType(venueId, type);
  }

  public List<String> getVenueCourtType(Integer venueId)
  {
    return courtRepository.getVenueCourtType(venueId);
  };
}
