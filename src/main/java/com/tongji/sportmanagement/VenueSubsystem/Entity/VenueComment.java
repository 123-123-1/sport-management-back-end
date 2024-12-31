package com.tongji.sportmanagement.VenueSubsystem.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "venue_comment")
public class VenueComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "time")
    private Instant time;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "venue_id", nullable = false)
    private Integer venue;

}