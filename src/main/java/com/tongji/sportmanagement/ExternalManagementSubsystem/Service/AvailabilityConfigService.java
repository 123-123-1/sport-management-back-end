package com.tongji.sportmanagement.ExternalManagementSubsystem.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.AvailabilityConfigInfoDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.CourtAvailabilityConfigDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.AvailabilityConfig;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.TimeslotConfig;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Repository.AvailabilityConfigRepository;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Repository.TimeslotConfigRepository;
import com.tongji.sportmanagement.VenueSubsystem.DTO.CourtDTO;
import com.tongji.sportmanagement.VenueSubsystem.Repository.CourtRepository;

import jakarta.transaction.Transactional;

@Service
public class AvailabilityConfigService
{
  @Autowired
  private ManagementUtilsService managementUtilsService;
  @Autowired
  private TimeslotConfigRepository timeslotConfigRepository;
  @Autowired
  private AvailabilityConfigRepository availabilityConfigRepository;
  @Autowired
  private CourtRepository courtRepository;

  private final List<String> repetitionDayEnum = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

  @Transactional
  public ResultMsg createAvailabilityConfig(AvailabilityConfigInfoDTO configInfo)
  {
    // 1. 创建availabilityConfig
    Integer repetitionBit = getRepetitionBit(configInfo.getRepetition());
    AvailabilityConfig availabilityConfig = new AvailabilityConfig(
      null,
      configInfo.getAvconfigName(),
      configInfo.getCourtId(),
      repetitionBit,
      configInfo.getDayAhead(),
      configInfo.getCreateHour()
    );
    availabilityConfigRepository.save(availabilityConfig);
    // 2. 创建timeslotConfig
    for (TimeslotConfig tsconfigItem : configInfo.getTsconfig()) {
      tsconfigItem.setAvconfigId(availabilityConfig.getAvconfigId());
    }
    timeslotConfigRepository.saveAll(configInfo.getTsconfig());
    // 3. 返回结果
    return new ResultMsg("编辑成功", 1);
  }

  public List<CourtAvailabilityConfigDTO> getAvailabilityConfig(Integer managerId) throws Exception
  {
    // 1. 找到目标场馆
    Integer venueId = managementUtilsService.getVenueIdByManager(managerId);
    // 2. 找到场馆的所有场地
    List<CourtDTO> venueCourts = courtRepository.findAllByVenueId(venueId);
    List<CourtAvailabilityConfigDTO> result = new ArrayList<CourtAvailabilityConfigDTO>();
    // 3. 对每一个场地查找对应配置项
    for (CourtDTO court : venueCourts) {
      List<AvailabilityConfigInfoDTO> courtConfigInfo = new ArrayList<AvailabilityConfigInfoDTO>();
      List<AvailabilityConfig> courtAvailabilityConfig = availabilityConfigRepository.findAllByCourtId(court.getCourtId());
      for (AvailabilityConfig availabilityConfig : courtAvailabilityConfig) {
        courtConfigInfo.add(new AvailabilityConfigInfoDTO(
          availabilityConfig.getAvconfigId(),
          availabilityConfig.getAvconfigName(),
          null,
          timeslotConfigRepository.findAllByAvconfigId(availabilityConfig.getAvconfigId()),
          getRepetitionStr(availabilityConfig.getRepetition()),
          availabilityConfig.getDayAhead(),
          availabilityConfig.getCreateHour()
        ));
      }
      result.add(new CourtAvailabilityConfigDTO(
        court.getCourtId(),
        court.getCourtName(),
        courtConfigInfo
      ));
    }
    return result;
  }

  @Transactional
  public ResultMsg patchAvailabilityConfig(AvailabilityConfigInfoDTO configInfo) throws Exception
  {
    // 1. 编辑目标配置项
    AvailabilityConfig availabilityConfig = new AvailabilityConfig(
      configInfo.getAvconfigId(),
      configInfo.getAvconfigName(),
      configInfo.getCourtId(),
      getRepetitionBit(configInfo.getRepetition()),
      configInfo.getDayAhead(),
      configInfo.getCreateHour()
    );
    availabilityConfigRepository.save(availabilityConfig);
    // 2. 创建新时间段、编辑已有时间段
    List<Integer> timeslotConfigIdList = new ArrayList<Integer>();
    for (TimeslotConfig timeslotConfig : configInfo.getTsconfig()) {
      timeslotConfig.setAvconfigId(availabilityConfig.getAvconfigId());
      timeslotConfigRepository.save(timeslotConfig);
      timeslotConfigIdList.add(timeslotConfig.getTsconfigId());
    }
    // 3. 删除更新后没有的时间段
    timeslotConfigRepository.deleteNonexistById(availabilityConfig.getAvconfigId(), timeslotConfigIdList);
    // 4. 返回结果
    return new ResultMsg("编辑成功", 1);
  }

  @Transactional
  public ResultMsg deleteAvailabilityConfig(Integer configId)
  {
    // 1. 删除所有时间段
    timeslotConfigRepository.deleteAllByAvconfigId(configId);;
    // 2. 删除配置项
    availabilityConfigRepository.deleteById(configId);
    return new ResultMsg("删除成功", 1);
  }

  Integer getRepetitionBit(List<String> repetitionStr)
  {
    Integer repetitionBit = 0;
    for (String str : repetitionStr) {
      repetitionBit |= (1 << repetitionDayEnum.indexOf(str));
    }
    return repetitionBit;
  }

  List<String> getRepetitionStr(Integer repetitionBit){
    List<String> repetitionStr = new ArrayList<String>();
    for(int i = 0; i < repetitionDayEnum.size(); i++){
      if((repetitionBit & (1 << i)) != 0){
        repetitionStr.add(repetitionDayEnum.get(i));
      }
    }
    return repetitionStr;
  }
}
