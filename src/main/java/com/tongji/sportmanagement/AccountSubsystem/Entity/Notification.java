package com.tongji.sportmanagement.AccountSubsystem.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    Integer notificationId;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("system")
    @Column(name = "type")
    NotificationType type;

    @Column(name="title")
    String title;

    @Column(name="content")
    String content;

    @Column(name="timestamp")
    Instant timestamp;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("unread")
    @Column(name="state")
    NotificationState state;

    @Column(name="user_id")
    Integer userId;

    @Column(name="group_id")
    Integer groupId;

}
