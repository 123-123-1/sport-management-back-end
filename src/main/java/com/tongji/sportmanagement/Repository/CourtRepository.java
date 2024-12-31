package com.tongji.sportmanagement.Repository;

import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtRepository extends CrudRepository<Court, Integer>
{
  @Query
  Iterable<Court> findAllByVenueId(int venueId);
}
