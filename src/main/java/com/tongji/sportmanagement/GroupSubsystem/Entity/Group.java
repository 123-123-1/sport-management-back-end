package com.tongji.sportmanagement.GroupSubsystem.Entity;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "`group`")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)
    private Integer groupId;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "name", length = 100)
    private String groupName;

    @Column(name = "chat_id")
    private Integer chatId;

    @Column(name = "creation_time")
    private Instant creationTime;
}