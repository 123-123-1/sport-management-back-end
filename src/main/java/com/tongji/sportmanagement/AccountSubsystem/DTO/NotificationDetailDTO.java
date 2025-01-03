package com.tongji.sportmanagement.AccountSubsystem.DTO;

import com.tongji.sportmanagement.AccountSubsystem.Entity.NotificationState;
import com.tongji.sportmanagement.AccountSubsystem.Entity.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDetailDTO {
    Integer notificationId;
    NotificationType type;
    String title;
    String content;
    Instant timestamp;
    NotificationState state;
}
