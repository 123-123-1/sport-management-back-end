package com.tongji.sportmanagement.ExternalManagementSubsystem.Repository;

import org.springframework.data.repository.CrudRepository;

import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiConfig;

public interface ApiConfigRepository extends CrudRepository<ApiConfig, Integer>
{
  
}