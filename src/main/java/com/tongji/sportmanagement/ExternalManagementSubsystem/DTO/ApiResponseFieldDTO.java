package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiResponseFieldDTO
{
  String name;        // 字段名称
  Boolean isRequired; // 是否必填
  String description; // 字段描述
  String type;        // 字段类型
  Object sample;      // 示例数据
}
