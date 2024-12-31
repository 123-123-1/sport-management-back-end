package com.tongji.sportmanagement.VenueSubsystem.Entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

enum VenueState
{
  open, closed
};

@Getter
@Setter
@Entity
@Table(name = "venue")
public class Venue
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer venueId;

  @Column(name = "name", length = 100)
  private String venueName;

  @Column(name = "location", length = 100)
  private String location;

  @Enumerated(EnumType.STRING)
  @ColumnDefault("closed")
  @Column(name = "state")
  private VenueState state;
  
  @Column(name = "contact_number")
  private String contactNumber;

  @Column(name = "image")
  private String image;

}
