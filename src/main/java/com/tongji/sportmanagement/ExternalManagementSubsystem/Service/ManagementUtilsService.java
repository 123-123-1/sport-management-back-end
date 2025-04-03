package com.tongji.sportmanagement.ExternalManagementSubsystem.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;
import com.tongji.sportmanagement.VenueSubsystem.Repository.VenueRepository;

@Service
public class ManagementUtilsService
{
  @Autowired
  private VenueRepository venueRepository;

  public Integer getVenueIdByManager(Integer managerId) throws Exception
  {
    Optional<Venue> venueOptional = venueRepository.findByManagerId(managerId);
    if(venueOptional.isEmpty()){
      throw new ServiceException(404, "未找到管理的场馆");
    }
    return venueOptional.get().getVenueId();
  }

}
