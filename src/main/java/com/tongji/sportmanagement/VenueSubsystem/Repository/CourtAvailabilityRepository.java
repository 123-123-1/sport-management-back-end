package com.tongji.sportmanagement.VenueSubsystem.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;

@Repository
public interface CourtAvailabilityRepository extends CrudRepository<CourtAvailability, Integer>
{
  @Query
  Iterable<CourtAvailability> findAllByTimeslotId(Integer timeslotId);

  @Query(value = "SELECT COUNT(*) FROM court_availability WHERE timeslot_id = :timeslotId", nativeQuery = true)
  Integer countByTimeslot(@Param("timeslotId")Integer timeslotId);

  @Query(value = "SELECT * FROM court_availability WHERE court_id = :courtId AND timeslot_id = :timeslotId", nativeQuery = true)
  Iterable<CourtAvailability> findAvailability(@Param("courtId") Integer courtId, @Param("timeslotId") Integer timeslotId);
}
