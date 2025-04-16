package com.tongji.sportmanagement.VenueSubsystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;

@Repository
public interface CourtAvailabilityRepository extends JpaRepository<CourtAvailability, Integer>
{
  @Query
  List<CourtAvailability> findAllByTimeslotId(Integer timeslotId);

  @Query(value = "SELECT COUNT(*) FROM court_availability WHERE timeslot_id = :timeslotId", nativeQuery = true)
  Integer countByTimeslot(@Param("timeslotId")Integer timeslotId);

  @Query("SELECT ca FROM CourtAvailability ca JOIN ca.timeslot t JOIN ca.court c WHERE ca.availabilityId = :availabilityId")
  CourtAvailability getAvailabilityFullInfo(@Param("availabilityId") Integer availabilityId);

  @Query(value = "SELECT * FROM court_availability WHERE court_id = :courtId AND timeslot_id = :timeslotId", nativeQuery = true)
  Iterable<CourtAvailability> findAvailability(@Param("courtId") Integer courtId, @Param("timeslotId") Integer timeslotId);

  @Query(value = "SELECT * FROM court_availability WHERE timeslot_id = :timeslotId AND state = :state", nativeQuery = true)
  Iterable<CourtAvailability> getAvailabilityByState(@Param("timeslotId") Integer timeslotId, @Param("state") String state);

  @Query(value = "SELECT EXISTS (SELECT * FROM court_availability WHERE court_id = :courtId)", nativeQuery = true)
  Long hasCourtAvailability(@Param("courtId") Integer courtId);

  @Query("SELECT t.venueId FROM CourtAvailability ca JOIN ca.timeslot t WHERE ca.availabilityId = :availabilityId")
  Integer findVenueByAvailability(@Param("availabilityId") Integer availabilityId);
}
