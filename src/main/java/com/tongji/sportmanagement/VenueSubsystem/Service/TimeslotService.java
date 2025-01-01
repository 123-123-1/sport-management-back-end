package com.tongji.sportmanagement.VenueSubsystem.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.VenueSubsystem.DTO.VenueTimeslotDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Timeslot;
import com.tongji.sportmanagement.VenueSubsystem.Repository.CourtAvailabilityRepository;
import com.tongji.sportmanagement.VenueSubsystem.Repository.TimeslotRepository;

@Service
public class TimeslotService
{
  @Autowired
  private TimeslotRepository timeslotRepository;
    @Autowired
  private CourtAvailabilityRepository courtAvailabilityRepository;

  // 获取场馆的所有Timeslot及其开放信息
  public List<VenueTimeslotDTO> getVenueTimeslots(int venueId, String date)
  {
    // 1. 获取场馆的所有时间段
    Instant start_date = Instant.parse(date + "T00:00:00Z");
    Instant end_date = start_date.atZone(ZoneId.systemDefault()).plusDays(1).toInstant();
    List<Timeslot> timeslots = (List<Timeslot>)timeslotRepository.findByDate(start_date, end_date);
    // 2. 获取时间段的所有可预约项
    List<VenueTimeslotDTO> result = new ArrayList<VenueTimeslotDTO>();
    for (Timeslot timeslot : timeslots) {
      List<CourtAvailability> availabilities = (List<CourtAvailability>)courtAvailabilityRepository.findAllByTimeslotId(timeslot.getTimeslotId());
      result.add(new VenueTimeslotDTO(timeslot, availabilities));
    }
    return result;
  }
}
