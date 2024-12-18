package com.tongji.sportmanagement.DTO;

import com.tongji.sportmanagement.Entity.Group;
import com.tongji.sportmanagement.Entity.GroupMember;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class CompleteGroup {
    private Group group;
    private ArrayList<GroupMember> members;
}
