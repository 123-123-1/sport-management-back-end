package com.tongji.sportmanagement.GroupSubsystem.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "group_record")
public class GroupRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_record_id", nullable = false)
    private Integer groupRecordId;

    @Column(name = "operator_id")
    private Integer operator;

    @Column(name = "target_id")
    private Integer target;

    @Column(name = "group_id")
    private Integer group;

    @Column(name = "time")
    private Instant time;

    @Size(max = 10)
    @Column(name = "operate_type", length = 10)
    private String operateType;

}