package com.tongji.sportmanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "chat_name", length = 50)
    private String chatName;

    @ColumnDefault("'groupChat'")
    @Lob
    @Column(name = "type")
    private String type;

    @Column(name = "creating_time")
    private Instant creatingTime;

    @Column(name = "photo", length = 100)
    private String photo;

}