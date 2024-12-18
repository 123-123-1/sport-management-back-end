package com.tongji.sportmanagement.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class GroupApplication {
    private String applicationID;
    private String type;
    private String applyInfo;
    private String groupID;
    private String state;
    private Date operationTime;
    private Date expirationTime;
    private String applicantID;
    private String recipientID;
}
