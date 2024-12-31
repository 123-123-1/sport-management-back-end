package com.tongji.sportmanagement.SocializeSubsystem.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", nullable = false)
    private Integer chatId;

    @Column(name = "chat_name", length = 50)
    private String chatName;

    @NotNull
    @ColumnDefault("'groupChat'")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ChatType type;


    @Column(name = "creation_time")
    private Instant creatingTime;

    @Column(name = "photo", length = 100)
    private String photo;

}
