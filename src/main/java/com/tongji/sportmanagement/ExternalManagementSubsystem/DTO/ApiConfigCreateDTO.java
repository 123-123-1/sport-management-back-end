package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiConfigCreateDTO
{
  ApiType type;
}
