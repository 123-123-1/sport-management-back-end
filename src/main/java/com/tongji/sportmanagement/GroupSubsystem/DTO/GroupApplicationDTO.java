package com.tongji.sportmanagement.GroupSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupApplicationDTO {
    private Integer groupId;
    private Integer applicantId;
    private String applyInfo;
}
