package com.tongji.sportmanagement.AccountSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationOperationDTO {
    int notificationId;
    String operation;
}
