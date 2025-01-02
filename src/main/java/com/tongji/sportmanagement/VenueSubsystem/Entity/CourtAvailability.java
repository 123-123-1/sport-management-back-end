package com.tongji.sportmanagement.VenueSubsystem.Entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "court_availability")
public class CourtAvailability
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "availability_id", nullable = false)
  Integer availabilityId;

  @Column(name = "timeslot_id", nullable = false)
  Integer timeslotId;

  @Column(name = "court_id", nullable = false)
  Integer courtId;

  @Enumerated(EnumType.STRING)
  @ColumnDefault("closed")
  @Column(name = "state")
  CourtAvailabilityState state;

  @Column(name = "price")
  Double price;
}

