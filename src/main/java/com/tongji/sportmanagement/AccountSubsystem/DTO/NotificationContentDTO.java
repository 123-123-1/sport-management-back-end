package com.tongji.sportmanagement.AccountSubsystem.DTO;

import com.tongji.sportmanagement.AccountSubsystem.Entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationContentDTO {
    NotificationType type;
    String title;
    String content;
    int userId;
}
