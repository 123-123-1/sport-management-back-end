package com.tongji.sportmanagement.VenueSubsystem.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tongji.sportmanagement.VenueSubsystem.Repository.VenueRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.SportManagementUtils;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.VenueSubsystem.DTO.VenueDetailDTO;
import com.tongji.sportmanagement.VenueSubsystem.DTO.VenueListDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;
import com.tongji.sportmanagement.VenueSubsystem.Entity.VenueState;

@Service
public class VenueService
{
  @Autowired
  private VenueRepository venueRepository;

  final int pageVenueCount = 10; // 一页场馆的数量

  // 获取所有场馆 or 根据名称关键字查找场馆
  public VenueListDTO getAllVenues(int page, String name)
  {
    List<Venue> venues;
    long total = 0;
    if(name.isBlank()){
      venues = venueRepository.findPageVenue((page - 1) * pageVenueCount, pageVenueCount);
      total = venueRepository.count();
    }
    else{
      venues = venueRepository.findVenueByName(name, (page - 1) * pageVenueCount, pageVenueCount);
      total = venueRepository.getVenueNameCount(name);
    }
    // 处理结果
    List<VenueDetailDTO> result = new ArrayList<VenueDetailDTO>();
    for (Venue venue : venues) {
      VenueDetailDTO resultVenue = new VenueDetailDTO();
      BeanUtils.copyProperties(venue, resultVenue);
      result.add(resultVenue);
    }
    return new VenueListDTO(total, page, result);
  }

  // 根据场馆ID获取场馆详细信息
  public VenueDetailDTO getVenueDetail(int venueId) throws Exception
  {
    Optional<Venue> venueOptional = venueRepository.findById(venueId);
    if(venueOptional.isEmpty()){
      throw new ServiceException(404, "场馆不存在");
    }
    VenueDetailDTO result = new VenueDetailDTO();
    BeanUtils.copyProperties(venueOptional.get(), result);
    return result;
  }

  // 创建场馆
  // public VenueInitResponseDTO createVenue(Venue venueInfo)
  // {
  //   venueRepository.save(venueInfo);
  //   VenueInitResponseDTO result = new VenueInitResponseDTO(venueInfo.getVenueId(), null, null);
  //   result.setToken(JwtService.getTokenById(venueInfo.getVenueId()).getToken());
  //   return result;
  // }

  public Venue getManagerVenue(Integer managerId) throws Exception
  {
    Optional<Venue> venueOptional = venueRepository.findByManagerId(managerId);
    if(venueOptional.isEmpty()){
      throw new ServiceException(404, "管理的场馆不存在");
    }
    return venueOptional.get();
  }

  // 创建默认场馆
  public void createVenue(Integer managerId)
  {
    Venue newVenue = new Venue(null, "未命名场馆", "", "", VenueState.closed, "", managerId);
    venueRepository.save(newVenue);
  }

  // 管理员修改场馆信息
  public ResultMsg patchVenue(Venue venueInfo, Integer managerId) throws Exception
  {
    venueInfo.setVenueId(null);
    venueInfo.setManagerId(managerId);
    Optional<Venue> venue = venueRepository.findById(managerId);
    if(!venue.isPresent()){
      throw new ServiceException(404, "未找到目标场馆");
    }
    Venue editedVenue = venue.get();
    SportManagementUtils.copyNotNullProperties(venueInfo, editedVenue);
    venueRepository.save(editedVenue);
    return new ResultMsg("成功编辑场馆信息", 1);
  }
}

