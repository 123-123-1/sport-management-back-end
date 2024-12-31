package com.tongji.sportmanagement.GroupSubsystem.DTO;

import com.tongji.sportmanagement.GroupSubsystem.Entity.Group;
import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupMember;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class CompleteGroup {
    private Group group;
    private ArrayList<GroupMember> members;
}
