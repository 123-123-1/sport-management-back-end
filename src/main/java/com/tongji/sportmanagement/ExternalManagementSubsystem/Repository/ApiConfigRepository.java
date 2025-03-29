package com.tongji.sportmanagement.ExternalManagementSubsystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiConfig;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiType;

public interface ApiConfigRepository extends JpaRepository<ApiConfig, Integer>
{
  @Query
  Optional<ApiConfig> findByVenueIdAndType(Integer venueId, ApiType type);
}