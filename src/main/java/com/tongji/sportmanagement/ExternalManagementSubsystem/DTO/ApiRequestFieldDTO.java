package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiRequestFieldDTO
{
  String name;        // 字段名称
  String description; // 字段描述
  String note;        // 备注
  String type;        // 字段类型
  Object sample;      // 示例数据
}
