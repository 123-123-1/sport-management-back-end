package com.tongji.sportmanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "violation")
public class Violation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false;

    @Column(name = "violation_count", nullable = false)
    private Integer violationCount;

    @Column(name = "unlock_time")
    private Instant unlockTime;

}