package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import java.util.Optional;

import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiConfigResponseDTO
{
  Integer exist;
  Optional<ApiConfig> configInfo;
}
