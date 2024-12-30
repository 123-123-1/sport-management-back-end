package com.tongji.sportmanagement.GroupSubsystem.Entity;

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
    @Column(name = "id", nullable = false)
    private Integer groupId;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "photo", length = 100)
    private String photo;

    @Column(name = "chat_id")
    private Integer chat;

}