package com.tongji.sportmanagement.VenueSubsystem.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.VenueSubsystem.Entity.Timeslot;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, Integer>
{
  // @Query(value = "SELECT * FROM timeslot WHERE venue_id = :venueId AND start_time BETWEEN :start_date AND :end_date ORDER BY start_time", nativeQuery = true)
  // List<Timeslot> findByDate(@Param("venueId") Integer venueId, @Param("start_date") Instant start_date, @Param("end_date") Instant end_date);
  @Query("""
      SELECT t
      FROM Timeslot t
      LEFT JOIN FETCH t.courtAvailabilities
      WHERE t.venueId = :venueId
      AND t.startTime BETWEEN :startDate AND :endDate
      ORDER BY t.startTime
    """
  )
  List<Timeslot> findByDate(@Param("venueId") Integer venueId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

  @Query(value = "SELECT timeslot_id FROM timeslot WHERE venue_id = :venueId AND start_time = :startTime AND end_time = :endTime", nativeQuery = true)
  Optional<Integer> findTimeslot(@Param("startTime") Instant startTime, @Param("endTime") Instant endTime, @Param("venueId") Integer venueId);

  @Query("SELECT EXISTS(SELECT 1 FROM Timeslot ts WHERE ts.venueId = :venueId AND ts.startTime BETWEEN :startDate AND :endDate) ")
  Boolean checkExistTimeslot(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate, @Param("venueId") Integer venueId);
}