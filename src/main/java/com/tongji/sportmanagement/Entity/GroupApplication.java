package com.tongji.sportmanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "groupapplication")
public class GroupApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'invited'")
    @Lob
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "apply_info", length = 100)
    private String applyInfo;

    @ColumnDefault("'waiting'")
    @Lob
    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "operation_time")
    private Instant operationTime;

    @Column(name = "expiration_time")
    private Instant expirationTime;

    @Column(name = "user_id", nullable = false)
    private Integer user;

    @Column(name = "group_id", nullable = false)
    private Integer group;

}