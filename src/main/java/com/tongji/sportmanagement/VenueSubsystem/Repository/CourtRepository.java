package com.tongji.sportmanagement.VenueSubsystem.Repository;

import com.tongji.sportmanagement.VenueSubsystem.DTO.CourtDTO;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtRepository extends JpaRepository<Court, Integer>
{
  @Query("""
    SELECT new com.tongji.sportmanagement.VenueSubsystem.DTO.CourtDTO(
      c.courtId,
      c.courtName,
      c.location,
      c.type,
      c.capacity,
      c.state,
      c.venueId
    )
    FROM Court c
    WHERE c.venueId = :venueId
  """)
  List<CourtDTO> findAllByVenueId(@Param("venueId") Integer venueId);

  @Query(value = "SELECT court_id FROM court WHERE court_name = :courtName AND venue_id = :venueId", nativeQuery = true)
  Optional<Integer> findVenueCourt(@Param("courtName") String courtName, @Param("venueId") Integer venueId);

  @Query("SELECT c.courtId FROM Court c WHERE c.venueId = :venueId AND c.type = :type")
  List<Integer> findAllByVenueIdAndType(@Param("venueId") Integer venueId, @Param("type")String type);

  @Query("SELECT c.type FROM Court c WHERE c.venueId = :venueId AND c.state = \"open\"")
  List<String> getVenueCourtType(@Param("venueId") Integer venueId);
}
