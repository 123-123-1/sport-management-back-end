package com.tongji.sportmanagement.VenueSubsystem.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.SportManagementUtils;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.VenueSubsystem.DTO.VenueTimeslotDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailabilityState;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailabiliyDTO;
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
  public List<VenueTimeslotDTO> getVenueTimeslots(Integer venueId, String date)
  {
    // 1. 获取场馆的所有时间段
    Instant startDate = Instant.parse(date + "T00:00:00Z");
    Instant endDate = startDate.atZone(ZoneId.systemDefault()).plusDays(1).toInstant();
    List<VenueTimeslotDTO> result = timeslotRepository.findByDate(venueId, startDate, endDate).stream().map(timeslot -> new VenueTimeslotDTO(
      timeslot.getTimeslotId(),
      timeslot.getStartTime(),
      timeslot.getEndTime(),
      timeslot.getCourtAvailabilities().stream().map(courtAvailability -> new CourtAvailabiliyDTO(
        courtAvailability.getAvailabilityId(),
        courtAvailability.getTimeslotId(),
        courtAvailability.getCourtId(),
        courtAvailability.getState(),
        courtAvailability.getPrice()
      )).toList()
    )).toList();
    return result;
    // List<Timeslot> timeslots = timeslotRepository.findByDate(venueId, startDate, endDate);
    // // 2. 获取时间段的所有可预约项
    // List<VenueTimeslotDTO> result = new ArrayList<VenueTimeslotDTO>();
    // for (Timeslot timeslot : timeslots) {
    //   List<CourtAvailability> availabilities = courtAvailabilityRepository.findAllByTimeslotId(timeslot.getTimeslotId());
    //   result.add(new VenueTimeslotDTO(timeslot.getTimeslotId(), timeslot.getStartTime(), timeslot.getEndTime(), availabilities));
    // }
    // return result;
  }

  public Timeslot createTimeslot(Timeslot timeslotInfo, Integer venueId)
  {
    timeslotInfo.setVenueId(venueId);
    timeslotRepository.save(timeslotInfo);
    return timeslotInfo;
  }

  public ResultMsg deleteTimeslot(Integer timeslotId, Instant startTime, Instant endTime, Integer venueId) throws Exception
  {
    if(timeslotId != null){
      if(courtAvailabilityRepository.countByTimeslot(timeslotId) > 0){
        throw new ServiceException(422, "该时间有对应预约项，请先删除预约项");
      }
      timeslotRepository.deleteById(timeslotId);
      return new ResultMsg("已删除时间段", 1);
    }
    else if(startTime != null && endTime != null){
      Optional<Integer> targetId = timeslotRepository.findTimeslot(startTime, endTime, venueId);
      if(!targetId.isPresent()){
        throw new ServiceException(404, "未找到对应时间段");
      }
      if(courtAvailabilityRepository.countByTimeslot(targetId.get()) > 0){
        throw new ServiceException(422, "该时间有对应预约项，请先删除预约项");
      }
      timeslotRepository.deleteById(targetId.get());
      return new ResultMsg("已删除时间段", 1);
    }
    else{
      throw new ServiceException(422, "未填写完整的时间段信息");
    }
  }

  public CourtAvailability createAvailability(CourtAvailability availability)
  {
    courtAvailabilityRepository.save(availability);
    return availability;
  }

  public ResultMsg patchAvailability(CourtAvailability availability) throws Exception
  {
    CourtAvailability editedAvailability = null;
    if(availability.getAvailabilityId() != null){
      Optional<CourtAvailability> targetAvailability = courtAvailabilityRepository.findById(availability.getAvailabilityId());
      if(!targetAvailability.isPresent()){
        throw new ServiceException(404, "未找到预约项");
      }
      editedAvailability = targetAvailability.get();
    }
    else{
      List<CourtAvailability> targetAvailability = ((List<CourtAvailability>)courtAvailabilityRepository.findAvailability(availability.getCourtId(), availability.getTimeslotId()));
      if(targetAvailability.size() == 0){
        throw new ServiceException(404, "未找到预约项");
      }
      editedAvailability = targetAvailability.get(0);
    }
    Integer timeslotId = editedAvailability.getTimeslotId();
    Integer courtId = editedAvailability.getCourtId();
    SportManagementUtils.copyNotNullProperties(availability, editedAvailability);
    // 避免更改这两个字段
    editedAvailability.setTimeslotId(timeslotId);
    editedAvailability.setCourtId(courtId);
    courtAvailabilityRepository.save(editedAvailability);
    return new ResultMsg("已编辑预约项", 1);
  }

  public ResultMsg deleteAvailability(Integer availabilityId, Integer courtId, Integer timeslotId) throws Exception
  {
    if(availabilityId != null){
      courtAvailabilityRepository.deleteById(availabilityId);
      return new ResultMsg("已删除预约项", 1);
    }
    else if(courtId != null && timeslotId != null){
      List<CourtAvailability> targetId = (List<CourtAvailability>)courtAvailabilityRepository.findAvailability(courtId, timeslotId);
      if(targetId.size() == 0){
        throw new ServiceException(404, "未找到预约项");
      }
      courtAvailabilityRepository.deleteById(targetId.get(0).getAvailabilityId());
      return new ResultMsg("已删除预约项", 1);
    }
    else{
      throw new ServiceException(422, "未完整填写预约项信息");
    }
  }

  public CourtAvailability getAvailability(Integer availabilityId) throws Exception
  {
    Optional<CourtAvailability> result = courtAvailabilityRepository.findById(availabilityId);
    if(!result.isPresent()){
      throw new ServiceException(404, "未找到开放时间段");
    }
    return result.get();
  }

  public List<CourtAvailability> getAvailabilityByState(Integer timeslotId, String state){
    return (List<CourtAvailability>)courtAvailabilityRepository.getAvailabilityByState(timeslotId, state);
  }

  public Timeslot getTimeslotById(Integer timeslotId) throws Exception
  {
    Optional<Timeslot> timeslot = timeslotRepository.findById(timeslotId);
    if(timeslot.isEmpty()){
      throw new ServiceException(404, "未找到开放时间段");
    }
    return timeslot.get();
  }

  public Integer findVenueByAvailabilityId(Integer availabilityId)
  {
    return courtAvailabilityRepository.findVenueByAvailability(availabilityId);
  }

  public CourtAvailability getAvailabilityFullInfo(Integer availabilityId)
  {
    return courtAvailabilityRepository.getAvailabilityFullInfo(availabilityId);
  }

  public void changeAvailabilityState(CourtAvailability courtAvailability, CourtAvailabilityState state){
      courtAvailability.setState(state);
      courtAvailabilityRepository.save(courtAvailability);
  }
}
