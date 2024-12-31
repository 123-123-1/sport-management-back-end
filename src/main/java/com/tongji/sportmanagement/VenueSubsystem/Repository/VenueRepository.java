package com.tongji.sportmanagement.VenueSubsystem.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;

@Repository
public interface VenueRepository extends CrudRepository<Venue, Integer>
{
  @Query(value = "SELECT * FROM venue LIMIT :count OFFSET :offset", nativeQuery = true)
  public Iterable<Venue> findPageVenue(@Param("offset") int offset, @Param("count") int count);
  
  @Query(value = "SELECT * FROM venue WHERE name LIKE %:venueName% LIMIT :count OFFSET :offset", nativeQuery = true)
  public Iterable<Venue> findVenueByName(@Param("venueName") String venueName,  @Param("offset") int offset, @Param("count") int count);

  @Query(value = "SELECT COUNT(*) FROM venue WHERE name LIKE %:venueName%", nativeQuery = true)
  public long getVenueNameCount(@Param("venueName") String venueName);
}