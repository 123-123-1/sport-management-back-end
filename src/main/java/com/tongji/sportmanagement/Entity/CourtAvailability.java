package com.tongji.sportmanagement.Entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

enum AvailabilityState
{
  reservable, matching, full, closed
}

@Entity
@Getter
@Setter
@Table(name = "court_availablity")
public class CourtAvailability
{
  @Id
  @Column(name = "availability_id", nullable = false)
  Integer availabilityId;

  @Column(name = "timeslot_id", nullable = false)
  Integer timeslotId;

  @Column(name = "court_id", nullable = false)
  Integer courtId;

  @Enumerated(EnumType.STRING)
  @ColumnDefault("closed")
  @Column(name = "state")
  AvailabilityState state;

  @Column(name = "price")
  Double price;
}

