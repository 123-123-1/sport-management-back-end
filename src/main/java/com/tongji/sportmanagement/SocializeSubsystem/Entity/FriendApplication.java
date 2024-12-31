package com.tongji.sportmanagement.SocializeSubsystem.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @Column(name = "friend_application_id", nullable = false)
    private Integer friendApplicationId;


    @Column(name = "apply_info", length = 100)
    private String applyInfo;

    @NotNull
    @ColumnDefault("'waiting'")
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private FriendApplicationState state;


    @Column(name = "operation_time")
    private Instant operationTime;

    @Column(name = "expiration_time")
    private Instant expirationTime;


    @Column(name = "applicant_id")
    private Integer applicantId;

    @Column(name = "reviewer_id")
    private Integer reviewerId;
}
