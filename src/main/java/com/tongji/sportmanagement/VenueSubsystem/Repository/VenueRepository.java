package com.tongji.sportmanagement.VenueSubsystem.Repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer>
{
  @Query(value = "SELECT * FROM venue LIMIT :count OFFSET :offset", nativeQuery = true)
  public List<Venue> findPageVenue(@Param("offset") int offset, @Param("count") int count);
  
  @Query(value = "SELECT * FROM venue WHERE name LIKE %:venueName% LIMIT :count OFFSET :offset", nativeQuery = true)
  public List<Venue> findVenueByName(@Param("venueName") String venueName,  @Param("offset") int offset, @Param("count") int count);

  @Query
  Page<Venue> findByVenueNameContaining(String venueName, Pageable pageable);

  @Query(value = "SELECT COUNT(*) FROM venue WHERE name LIKE %:venueName%", nativeQuery = true)
  public long getVenueNameCount(@Param("venueName") String venueName);
  
  public Optional<Venue> findByManagerId(Integer managerId);
}