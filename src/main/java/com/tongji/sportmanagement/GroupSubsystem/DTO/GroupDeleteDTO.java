package com.tongji.sportmanagement.GroupSubsystem.DTO;


import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

@Data
public class GroupDeleteDTO {
    private Integer groupId;
    private Integer userId;
}
