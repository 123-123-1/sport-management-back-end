package com.tongji.sportmanagement.ReservationSubsystem.Entity;

import com.tongji.sportmanagement.GroupSubsystem.Entity.Group;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "group_reservation")
public class GroupReservation
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_reservation_id", nullable = false)
  Integer groupReservationId;

  @Column(name = "group_id")
  Integer groupId;

  @Column(name = "reservation_id", unique = true)
  Integer reservationId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "group_id",
      referencedColumnName = "group_id",
      insertable = false,
      updatable = false
  )
  Group group;

  public GroupReservation(Integer groupId, Integer reservationId)
  {
    this.groupId = groupId;
    this.reservationId = reservationId;
    this.groupReservationId = null;
  }
}
