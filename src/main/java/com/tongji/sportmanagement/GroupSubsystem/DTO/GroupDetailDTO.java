package com.tongji.sportmanagement.GroupSubsystem.DTO;


import com.tongji.sportmanagement.Common.DTO.LittleUserDTO;
import lombok.Data;

import java.util.List;

@Data
public class GroupDetailDTO {
    private int groupId;
    private String groupName;
    private String description;
    private Integer chatId;
    private List<LittleUserDTO>members;
}
