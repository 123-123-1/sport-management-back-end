package com.tongji.sportmanagement.SocializeSubsystem.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "friend_application")
public class FriendApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer friendApplicationId;

    @Column(name = "type", nullable = false, length = 10)
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

    @Column(name = "applicant", nullable = false)
    private Integer applicant;

    @Column(name = "reviewer", nullable = false)
    private Integer reviewer;

}