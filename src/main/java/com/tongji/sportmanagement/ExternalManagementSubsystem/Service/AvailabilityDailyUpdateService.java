package com.tongji.sportmanagement.ExternalManagementSubsystem.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.AvailabilityConfig;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.TimeslotConfig;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Repository.AvailabilityConfigRepository;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Repository.TimeslotConfigRepository;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailabilityState;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Timeslot;
import com.tongji.sportmanagement.VenueSubsystem.Repository.CourtAvailabilityRepository;
import com.tongji.sportmanagement.VenueSubsystem.Repository.TimeslotRepository;
import com.tongji.sportmanagement.VenueSubsystem.Service.CourtService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
class TimeslotAvailabilityDTO
{
  Timeslot timeslot;
  Double price;
};


@Service
public class AvailabilityDailyUpdateService
{
  @Autowired
  private CourtService courtService;

  @Autowired
  private TimeslotRepository timeslotRepository;
  @Autowired
  private CourtAvailabilityRepository courtAvailabilityRepository;
  @Autowired
  private AvailabilityConfigRepository availabilityConfigRepository;
  @Autowired
  private TimeslotConfigRepository timeslotConfigRepository;

  @Scheduled(cron = "0 0 * * * ?")
  public void createAvailability()
  {
    LocalDateTime now = LocalDateTime.now();
    Instant nowDate = now.plusHours(-now.getHour()).plusMinutes(-now.getMinute()).plusNanos(-now.getNano()).toInstant(ZoneOffset.ofHours(0));
    Integer weekday = now.getDayOfWeek().get(ChronoField.DAY_OF_WEEK) - 1;
    List<AvailabilityConfig> availabilityConfig = availabilityConfigRepository.getUpdatingVenueAvailabilities(weekday, now.getHour());
    for(AvailabilityConfig avconfig : availabilityConfig){
      try{
        Court court = courtService.getCourtById(avconfig.getCourtId());
        Integer venueId = court.getVenueId();
        Instant targetDate = nowDate.atZone(ZoneId.systemDefault()).plusDays(avconfig.getDayAhead()).toInstant();
        // Instant endDate = targetDate.atZone(ZoneId.systemDefault()).plusDays(1).toInstant();
        // if(timeslotRepository.checkExistTimeslot(targetDate, endDate, venueId)){
        //   continue;
        // }
        
        // 添加开放时间段
        List<TimeslotConfig> timeslotConfigs = timeslotConfigRepository.findAllByAvconfigId(avconfig.getAvconfigId());
        List<TimeslotAvailabilityDTO> timeslotAvailabilities = new ArrayList<TimeslotAvailabilityDTO>();
        for (TimeslotConfig tsconfig: timeslotConfigs) {
          Instant startTime = targetDate.plus(Duration.ofHours(tsconfig.getStartTime().getHour())).plus(Duration.ofMinutes(tsconfig.getStartTime().getMinute()));
          Instant endTime = targetDate.plus(Duration.ofHours(tsconfig.getEndTime().getHour())).plus(Duration.ofMinutes(tsconfig.getEndTime().getMinute()));
          Optional<Timeslot> timeslotOptional = timeslotRepository.getTimeslotByDate(startTime, endTime, venueId);
          if(timeslotOptional.isEmpty()){
            Timeslot ts = new Timeslot(
              null,
              startTime,
              endTime,
              venueId,
              null
            );
            timeslotRepository.save(ts);
            timeslotAvailabilities.add(new TimeslotAvailabilityDTO(ts, tsconfig.getPrice()));
          }
          else{
            timeslotAvailabilities.add(new TimeslotAvailabilityDTO(timeslotOptional.get(), tsconfig.getPrice()));
          }
        }

        // List<TimeslotAvailabilityDTO> timeslotAvailabilities = timeslotConfigs.stream().map(tsconfig -> {
        //   Instant startTime = targetDate.plus(Duration.ofHours(tsconfig.getStartTime().getHour())).plus(Duration.ofMinutes(tsconfig.getStartTime().getMinute()));
        //   Instant endTime = targetDate.plus(Duration.ofHours(tsconfig.getEndTime().getHour())).plus(Duration.ofMinutes(tsconfig.getEndTime().getMinute()));
        //   Optional<Timeslot> timeslotOptional = timeslotRepository.getTimeslotByDate(startTime, endTime, venueId);
        //   if(timeslotOptional.isEmpty()){
        //     return new TimeslotAvailabilityDTO(new Timeslot(
        //       null,
        //       startTime,
        //       endTime,
        //       venueId,
        //       null
        //     ), tsconfig.getPrice());
        //   }
        //   else{
        //     return new TimeslotAvailabilityDTO(timeslotOptional.get(), tsconfig.getPrice());
        //   }
        // }).toList();
        // timeslotRepository.saveAll(timeslotAvailabilities.stream().map(tsav -> tsav.getTimeslot()).toList());
        List<CourtAvailability> courtAvailabilities = timeslotAvailabilities.stream().map(tsav -> new CourtAvailability(
          null,
          tsav.getTimeslot().getTimeslotId(),
          avconfig.getCourtId(),
          CourtAvailabilityState.reserveable,
          tsav.getPrice(),
          null,
          null
        )).toList();
        courtAvailabilityRepository.saveAll(courtAvailabilities);
      }
      catch(Exception e){
        e.printStackTrace();
        continue;
      }
    }
  }
}
