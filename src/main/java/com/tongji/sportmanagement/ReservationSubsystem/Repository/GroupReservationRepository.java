package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.GroupReservation;

@Repository
public interface GroupReservationRepository extends CrudRepository<GroupReservation, Integer>
{
  @Query(value = "SELECT * FROM group_reservation WHERE reservation_id = :reservationId", nativeQuery = true)
  Optional<GroupReservation> findByReservationId(@Param("reservationId") Integer reservationId);
}
