package com.tongji.sportmanagement.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;

@Repository
public interface CourtAvailabilityRepository extends CrudRepository<CourtAvailability, Integer>
{
  @Query
  Iterable<CourtAvailability> findAllByTimeslotId(Integer timeslotId);
}
