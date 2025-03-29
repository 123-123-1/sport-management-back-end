package com.tongji.sportmanagement.ExternalManagementSubsystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiConfig;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiType;

public interface ApiConfigRepository extends CrudRepository<ApiConfig, Integer>
{
  @Query
  Optional<ApiConfig> findByVenueIdAndType(Integer venueId, ApiType type);
}