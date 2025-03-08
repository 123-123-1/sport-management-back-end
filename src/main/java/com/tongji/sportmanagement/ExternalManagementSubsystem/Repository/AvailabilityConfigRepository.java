package com.tongji.sportmanagement.ExternalManagementSubsystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.AvailabilityConfig;

public interface AvailabilityConfigRepository extends CrudRepository<AvailabilityConfig, Integer>
{
  @Query
  public List<AvailabilityConfig> findAllByCourtId(Integer courtId);
}