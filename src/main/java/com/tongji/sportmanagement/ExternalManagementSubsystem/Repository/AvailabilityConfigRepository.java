package com.tongji.sportmanagement.ExternalManagementSubsystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.AvailabilityConfig;

public interface AvailabilityConfigRepository extends JpaRepository<AvailabilityConfig, Integer>
{
  @Query
  public List<AvailabilityConfig> findAllByCourtId(Integer courtId);
}