package com.tongji.sportmanagement.VenueSubsystem.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

// import com.tongji.sportmanagement.Entity.User;

@Getter
@Setter
@Entity
@Table(name = "venuecomment")
public class VenueComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "time")
    private Instant time;

    // @OneToOne(fetch = FetchType.LAZY, optional = false)
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    @Column(name = "venue_id", nullable = false)
    private Integer venue;

}