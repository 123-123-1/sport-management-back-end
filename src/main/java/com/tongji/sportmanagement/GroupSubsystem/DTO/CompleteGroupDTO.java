package com.tongji.sportmanagement.GroupSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CompleteGroupDTO {
    private Integer creatorId;
    private String description;
    private String groupName;
    private List<Integer> members;
}
