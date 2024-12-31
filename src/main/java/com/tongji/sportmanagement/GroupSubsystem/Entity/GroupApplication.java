package com.tongji.sportmanagement.GroupSubsystem.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "group_application")
public class GroupApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_application_id", nullable = false)
    private Integer groupApplicationId;

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

    @Column(name = "group_id", nullable = false)
    private Integer group;

    @NotNull
    @Column(name = "applicant_id", nullable = false)
    private Integer applicant;

    @Column(name = "reviewer_id")
    private Integer reviewer;

}