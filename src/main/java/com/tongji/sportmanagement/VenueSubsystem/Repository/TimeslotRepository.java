package com.tongji.sportmanagement.VenueSubsystem.Repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.VenueSubsystem.Entity.Timeslot;

@Repository
public interface TimeslotRepository extends CrudRepository<Timeslot, Integer>
{
  @Query(value = "SELECT * FROM timeslot WHERE venue_id = :venueId AND start_time BETWEEN :start_date AND :end_date ORDER BY start_time", nativeQuery = true)
  Iterable<Timeslot> findByDate(@Param("venueId") Integer venueId, @Param("start_date") Instant start_date, @Param("end_date") Instant end_date);

  @Query(value = "SELECT timeslot_id FROM timeslot WHERE venue_id = :venueId AND start_time = :startTime AND end_time = :endTime", nativeQuery = true)
  Optional<Integer> findTimeslot(@Param("startTime") Instant startTime, @Param("endTime") Instant endTime, @Param("venueId") Integer venueId);
}