package com.tongji.sportmanagement.VenueSubsystem.Entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

enum CourtState
{
    open, closed
}

@Getter
@Setter
@Entity
@Table(name = "court")
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "court_id", nullable = false)
    private Integer courtId;

    @Column(name = "court_name", length = 50)
    private String courtName;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "type", length = 20)
    private String type;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("closed")
    @Column(name = "court_state")
    private CourtState state;

    @Column(name = "venue_id", nullable = false)
    private Integer venueId;

}