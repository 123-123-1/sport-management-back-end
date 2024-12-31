package com.tongji.sportmanagement.GroupSubsystem.DTO;

import com.tongji.sportmanagement.GroupSubsystem.Entity.Group;
import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupMember;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CompleteGroup {

    private String description;
    private String name;
    private List<Integer> members;
}
