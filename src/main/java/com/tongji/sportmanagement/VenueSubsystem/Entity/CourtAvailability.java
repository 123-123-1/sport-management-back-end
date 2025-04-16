package com.tongji.sportmanagement.VenueSubsystem.Entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "court_id",
      referencedColumnName = "court_id",
      insertable = false,
      updatable = false
  )
  Court court;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "timeslot_id",
      referencedColumnName = "timeslot_id",
      insertable = false,
      updatable = false
  )
  Timeslot timeslot;
}

