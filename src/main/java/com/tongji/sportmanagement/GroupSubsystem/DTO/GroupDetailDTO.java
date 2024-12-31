package com.tongji.sportmanagement.GroupSubsystem.DTO;

import lombok.Data;

import java.util.List;

@Data
public class GroupDetailDTO {
    private int groupId;
    private String groupName;
    private String description;
    private Integer chatId;
    private List<GroupMemberDetailDTO>members;
}
