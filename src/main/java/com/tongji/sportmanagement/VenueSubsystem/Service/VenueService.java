package com.tongji.sportmanagement.VenueSubsystem.Service;

import java.util.List;
import java.util.Optional;

import com.tongji.sportmanagement.VenueSubsystem.Repository.VenueRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.VenueSubsystem.DTO.VenueListDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;

@Service
public class VenueService
{
  @Autowired
  private VenueRepository venueRepository;

  final int pageVenueCount = 10; // 一页场馆的数量

  // 获取所有场馆 or 根据名称关键字查找场馆
  public VenueListDTO getAllVenues(int page, String name)
  {
    List<Venue> result;
    long total = 0;
    if(name.isBlank()){
      result = (List<Venue>) venueRepository.findPageVenue((page - 1) * pageVenueCount, pageVenueCount);
      total = venueRepository.count();
    }
    else{
      result = (List<Venue>) venueRepository.findVenueByName(name, (page - 1) * pageVenueCount, pageVenueCount);
      total = venueRepository.getVenueNameCount(name);
    }
    return new VenueListDTO(total, page, result);
  }

  // 根据场馆ID获取场馆详细信息
  public Venue getVenueDetail(int venueId) throws Exception
  {
    Optional<Venue> result = venueRepository.findById(venueId);
    if(!result.isPresent()){
      throw new ServiceException(404, "场馆不存在");
    }
    return result.get();
  }
}

