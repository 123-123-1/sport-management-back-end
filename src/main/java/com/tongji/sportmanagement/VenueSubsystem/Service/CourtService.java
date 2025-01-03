package com.tongji.sportmanagement.VenueSubsystem.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.SportManagementUtils;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.VenueSubsystem.DTO.CourtResponseDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;
import com.tongji.sportmanagement.VenueSubsystem.Repository.CourtRepository;

@Service
public class CourtService
{
  @Autowired
  private CourtRepository courtRepository;

  // 根据场馆ID获取场馆的所有场地
  public List<Court> getVenueCourts(int venueId)
  {
    return (List<Court>)courtRepository.findAllByVenueId(venueId);
  }

  // 批量创建场馆的场地
  public List<CourtResponseDTO> createCourts(List<Court> courts, int venueId)
  {
    for (Court court : courts) {
      court.setVenueId(venueId);
    }
    courtRepository.saveAll(courts);
    List<CourtResponseDTO> result = new ArrayList<CourtResponseDTO>();
    for (Court court : courts) {
      result.add(new CourtResponseDTO(court.getCourtId(), court.getCourtName()));
    }
    return result;
  }

  public CourtResponseDTO createCourt(Court courtInfo, int venueId)
  {
    courtInfo.setVenueId(venueId);
    courtRepository.save(courtInfo);
    return new CourtResponseDTO(courtInfo.getCourtId(), courtInfo.getCourtName());
  }

  public ResultMsg patchCourt(Court courtInfo, int venueId) throws Exception
  {
    courtInfo.setVenueId(venueId);
    Optional<Court> court = courtRepository.findById(courtInfo.getCourtId());
    if(!court.isPresent()){
      throw new ServiceException(404, "未找到对应场地");
    }
    Court editedCourt = court.get();
    SportManagementUtils.copyNotNullProperties(courtInfo, editedCourt);
    courtRepository.save(editedCourt);
    return new ResultMsg("已成功编辑场地信息", 1);
  }

  public ResultMsg deleteCourt(Integer courtId, String courtName, Integer venueId) throws Exception
  {
    if(courtId != null){
      courtRepository.deleteById(courtId);
    }
    else{
      Optional<Integer> targetId = courtRepository.findVenueCourt(courtName, venueId);
      if(!targetId.isPresent()){
        throw new ServiceException(404, "未找到对应场地");
      }
      courtRepository.deleteById(targetId.get());
    }
    return new ResultMsg("已成功删除场地", 1);
  }

  public Court getCourtById(Integer courtId) throws Exception
  {
    Optional<Court> result = courtRepository.findById(courtId);
    if(result.isEmpty()){
      System.out.println("未找到场地：" + courtId);
      throw new ServiceException(404, "未找到场地");
    }
    return result.get();
  }
}
