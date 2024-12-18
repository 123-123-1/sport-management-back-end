package com.tongji.sportmanagement.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class FriendApplication {
  private String applicationID;
  private String applyInfo;
  private String state;
  private Date operationTime;
  private Date expirationTime;
  private String applicantID;
  private String recipientID;
}
