package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.GroupSubsystem.Entity.Group;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.GroupReservation;

@Repository
public interface GroupReservationRepository extends JpaRepository<GroupReservation, Integer>
{
  // @Query(value = "SELECT * FROM group_reservation WHERE reservation_id = :reservationId", nativeQuery = true)
  // Optional<GroupReservation> findByReservationId(@Param("reservationId") Integer reservationId);
  @Query("SELECT g FROM GroupReservation gr LEFT JOIN gr.group g WHERE gr.reservationId = :reservationId")
  Group getReservationGroup(@Param("reservationId") Integer reservationId);
}
