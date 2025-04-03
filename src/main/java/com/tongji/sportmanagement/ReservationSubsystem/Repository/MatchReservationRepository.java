package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.MatchReservation;

@Repository
public interface MatchReservationRepository extends JpaRepository<MatchReservation, Integer>
{
  @Query
  Optional<MatchReservation> findByReservationId(Integer reservationId);

  @Query(value = 
    "SELECT mr.* " + //
    "FROM court c " + //
    "JOIN court_availability ca ON c.court_id = ca.court_id " + //
    "JOIN reservation r ON ca.availability_id = r.availability_id " + //
    "  AND ca.timeslot_id = :timeslotId " + //
    "JOIN match_reservation mr ON r.reservation_id = mr.reservation_id " + //
    "WHERE c.type = :courtType " + //
    "  AND (mr.reserved_count + :count) <= c.capacity " + //
    "  AND c.venue_id = :venueId" + //
    "  AND " + //
    "ORDER BY mr.reserved_count " + //
    "LIMIT 1",
  nativeQuery = true)
  Optional<MatchReservation> findMatch(@Param("venueId") Integer venueId, @Param("timeslotId") Integer timeslotId, @Param("courtType") String courtType);
}