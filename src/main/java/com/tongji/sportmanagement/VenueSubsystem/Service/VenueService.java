package com.tongji.sportmanagement.VenueSubsystem.Service;

import java.util.Optional;

import com.tongji.sportmanagement.VenueSubsystem.Repository.VenueRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tongji.sportmanagement.Common.OssService;
import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.SportManagementUtils;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.VenueSubsystem.DTO.VenueDetailDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;
import com.tongji.sportmanagement.VenueSubsystem.Entity.VenueState;

@Service
public class VenueService
{
  @Autowired
  private VenueRepository venueRepository;
  @Autowired
  private OssService ossService;

  final int pageVenueCount = 10; // 一页场馆的数量

  // 获取所有场馆 or 根据名称关键字查找场馆
  // public VenueListDTO getAllVenues(int page, String name)
  // {
  //   List<Venue> venues;
  //   long total = 0;
  //   if(name.isBlank()){
  //     venues = venueRepository.findPageVenue((page - 1) * pageVenueCount, pageVenueCount);
  //     total = venueRepository.count();
  //   }
  //   else{
  //     venues = venueRepository.findVenueByName(name, (page - 1) * pageVenueCount, pageVenueCount);
  //     total = venueRepository.getVenueNameCount(name);
  //   }
  //   // 处理结果
  //   List<VenueDetailDTO> result = new ArrayList<VenueDetailDTO>();
  //   for (Venue venue : venues) {
  //     VenueDetailDTO resultVenue = new VenueDetailDTO();
  //     BeanUtils.copyProperties(venue, resultVenue);
  //     resultVenue.setImage(getVenueImage(venue.getVenueId()));
  //     result.add(resultVenue);
  //   }
  //   return new VenueListDTO(total, page, result);
  // }

  // // 根据场馆ID获取场馆详细信息
  // public VenueDetailDTO getVenueDetail(int venueId) throws Exception
  // {
  //   Optional<Venue> venueOptional = venueRepository.findById(venueId);
  //   if(venueOptional.isEmpty()){
  //     throw new ServiceException(404, "场馆不存在");
  //   }
  //   VenueDetailDTO result = new VenueDetailDTO();
  //   BeanUtils.copyProperties(venueOptional.get(), result);
  //   result.setImage(getVenueImage(venueId));
  //   return result;
  // }

  
  // 获取所有场馆 or 根据名称关键字查找场馆
  public Page<VenueDetailDTO> getAllVenues(Integer page, String name)
  {
    Pageable pageable = PageRequest.of(page, pageVenueCount);
    Page<Venue> venues;
    if(name.isBlank()){
      venues = venueRepository.findAll(pageable);
    }
    else{
      venues = venueRepository.findByVenueNameContaining(name, pageable);
    }

    // 处理结果
    return venues.map(venue -> {
        VenueDetailDTO venueDetail = new VenueDetailDTO();
        BeanUtils.copyProperties(venue, venueDetail);
        venueDetail.setImage(getVenueImage(venue.getVenueId()));
        return venueDetail;
      }
    );
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
    result.setImage(getVenueImage(venueId));
    return result;
  }

  // 根据管理员ID查找场馆
  public VenueDetailDTO getManagerVenue(Integer managerId) throws Exception
  {
    Optional<Venue> venueOptional = venueRepository.findByManagerId(managerId);
    if(venueOptional.isEmpty()){
      throw new ServiceException(404, "管理的场馆不存在");
    }
    VenueDetailDTO result = new VenueDetailDTO();
    BeanUtils.copyProperties(venueOptional.get(), result);
    result.setImage(getVenueImage(result.getVenueId()));
    return result;
  }

  // 创建默认场馆
  public void createVenue(Integer managerId)
  {
    Venue newVenue = new Venue(null, "未命名场馆", "", "", VenueState.closed, "", managerId);
    venueRepository.save(newVenue);
    String venueImageName = "venue_" + newVenue.getVenueId();
    ossService.copyDefault("default_venue", venueImageName);
  }

  // 管理员修改场馆信息
  public ResultMsg patchVenue(Venue venueInfo, Integer managerId) throws Exception
  {
    venueInfo.setVenueId(null);
    venueInfo.setManagerId(managerId);
    Optional<Venue> venue = venueRepository.findByManagerId(managerId);
    if(venue.isEmpty()){
      throw new ServiceException(404, "未找到目标场馆");
    }
    Venue editedVenue = venue.get();
    SportManagementUtils.copyNotNullProperties(venueInfo, editedVenue);
    venueRepository.save(editedVenue);
    return new ResultMsg("成功编辑场馆信息", 1);
  }

  // 上传图片
  public ResultMsg updateVenueImage(MultipartFile image, Integer managerId) throws Exception
  {
    Optional<Venue> targetVenue = venueRepository.findByManagerId(managerId);
    if(targetVenue.isEmpty()){
      throw new ServiceException(404, "未找到管理的场馆");
    }
    String venueImageName = "venue_" + targetVenue.get().getVenueId();
    ossService.deleteFile(venueImageName);
    ossService.uploadFile(image.getInputStream(), venueImageName);
    return new ResultMsg(ossService.getFileLink(venueImageName), 1);
  }

  // 获取场馆图片
  String getVenueImage(Integer venueId)
  {
    String venueImageName = "venue_" + venueId;
    return ossService.getFileLink(venueImageName);
  }
}

