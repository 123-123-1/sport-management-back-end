package com.tongji.sportmanagement.SocializeSubsystem.Entity;

import com.tongji.sportmanagement.Common.Entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Integer messageId;

    @Column(name = "time")
    private Instant time;

    @Column(name = "content")
    private String content;

    @Column(name = "chat_id", nullable = false)
    private Integer chatId;

    @Column(name = "user_id")
    private Integer userId;

}