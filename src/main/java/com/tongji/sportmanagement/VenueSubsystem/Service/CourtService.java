package com.tongji.sportmanagement.VenueSubsystem.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
