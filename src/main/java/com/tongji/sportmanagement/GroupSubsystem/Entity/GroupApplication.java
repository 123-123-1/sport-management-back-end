package com.tongji.sportmanagement.GroupSubsystem.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    @ColumnDefault("'invited'")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private GroupApplicationType type;

    @Size(max = 100)
    @Column(name = "apply_info", length = 100)
    private String applyInfo;

    @NotNull
    @ColumnDefault("'waiting'")
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private GroupApplicationState state;

    @Column(name = "operation_time")
    private Instant operationTime;

    @Column(name = "expiration_time")
    private Instant expirationTime;

    @NotNull
    @Column(name = "applicant_id", nullable = false)
    private Integer applicant;

    @NotNull
    @Column(name = "group_id", nullable = false)
    private Integer group;

    @Column(name = "reviewer_id")
    private Integer reviewer;


}
