package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiConfigFieldsDTO
{
  ApiRequestFieldDTO[] requestFields;
  ApiResponseFieldDTO[] responseFields;  
}
