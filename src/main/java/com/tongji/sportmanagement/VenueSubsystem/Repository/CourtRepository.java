package com.tongji.sportmanagement.VenueSubsystem.Repository;

import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtRepository extends CrudRepository<Court, Integer>
{
  @Query
  Iterable<Court> findAllByVenueId(int venueId);

  @Query(value = "SELECT court_id FROM court WHERE court_name = :courtName AND venue_id = :venueId", nativeQuery = true)
  Optional<Integer> findVenueCourt(@Param("courtName") String courtName, @Param("venueId") Integer venueId);
}
