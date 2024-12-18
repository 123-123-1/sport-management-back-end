package com.tongji.sportmanagement.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class GroupMemberRecord {
    private String recordID;
    private Date time;
    private String operationType;
}
