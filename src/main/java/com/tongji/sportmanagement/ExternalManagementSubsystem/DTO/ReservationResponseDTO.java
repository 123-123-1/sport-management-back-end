package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

// 场地管理方收到预约请求时，返回的数据结构

@Data
@AllArgsConstructor
public class ReservationResponseDTO
{
  int status;
  String msg;
}
