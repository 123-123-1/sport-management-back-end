package com.tongji.sportmanagement.GroupSubsystem.DTO;

import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupMemberRole;
import lombok.Data;


@Data
public class RoleDTO {
    Integer operatorId;
    Integer targetId;
    Integer groupId;
    GroupMemberRole role;
}
