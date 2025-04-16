package com.tongji.sportmanagement.ReservationSubsystem.Entity;

import org.hibernate.annotations.ColumnDefault;

import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "reservation")
public class Reservation
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_id", nullable = false)
  private Integer reservationId;

  @Enumerated(EnumType.STRING)
  @ColumnDefault("individual")
  @Column(name = "type")
  private ReservationType type;

  @Enumerated(EnumType.STRING)
  @ColumnDefault("normal")
  @Column(name = "state")
  private ReservationState state;

  @Column(name = "availability_id")
  private Integer availabilityId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "availability_id",
      referencedColumnName = "availability_id",
      insertable = false,
      updatable = false
  )
  private CourtAvailability courtAvailability;

  @OneToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(
      name = "reservation_id",
      referencedColumnName = "reservation_id",
      insertable = false,
      updatable = false
  )
  private MatchReservation matchReservation;

  @OneToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(
      name = "reservation_id",
      referencedColumnName = "reservation_id",
      insertable = false,
      updatable = false
  )
  private GroupReservation groupReservation;

  public Reservation(ReservationType type, Integer availabilityId)
  {
    this.reservationId = null;
    this.type = type;
    this.availabilityId = availabilityId;
    if(type == ReservationType.match){
      this.state = ReservationState.matching;
    }
    else{
      this.state = ReservationState.normal;
    }
  }
}
