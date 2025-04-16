package com.tongji.sportmanagement.ExternalManagementSubsystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.AvailabilityConfig;

public interface AvailabilityConfigRepository extends JpaRepository<AvailabilityConfig, Integer>
{
  @Query
  public List<AvailabilityConfig> findAllByCourtId(Integer courtId);

  @Query(
    value = "SELECT * FROM availability_config ac WHERE ((1 << (ac.day_ahead + :weekday) MOD 7) & ac.repetition <> 0) AND ac.create_hour = :createHour",
    nativeQuery = true
  )
  public List<AvailabilityConfig> getUpdatingVenueAvailabilities(@Param("weekday") Integer weekday, @Param("createHour") Integer createHour);

}