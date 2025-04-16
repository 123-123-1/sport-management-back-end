package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationManagerMetaDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationStateCountDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationBasicDTO;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.Reservation;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation>
{
  // @Query
  // Optional<Reservation> findByAvailabilityId(Integer availabilityId);
  @Query("SELECT r FROM Reservation r WHERE r.availabilityId = :availabilityId AND r.state = \"matching\"")
  Optional<Reservation> getMatchingReservation(Integer availabilityId);

  // @Query(
  //   value = "SELECT " + //
  //           "    r.reservation_id, " + //
  //           "    r.type, " + //
  //           "    r.availability_id, " + //
  //           "    ca.court_id, " + //
  //           "    c.court_name, " + //
  //           "    c.venue_id, " + //
  //           "    v.`name` AS venue_name, " + //
  //           "    t.start_time, " + //
  //           "    t.end_time, " + //
  //           "    gr.group_id, " + //
  //           "    g.`name` AS group_name, " + //
  //           "    mr.reserved_count, " + //
  //           "    mr.expiration_time " + //
  //           "FROM reservation r " + //
  //           "JOIN court_availability ca ON r.availability_id = ca.availability_id " + //
  //           "JOIN court c ON ca.court_id = c.court_id " + //
  //           "JOIN venue v ON c.venue_id = v.venue_id " + //
  //           "JOIN timeslot t ON ca.timeslot_id = t.timeslot_id " + //
  //           "LEFT JOIN group_reservation gr " + //
  //           "    ON r.reservation_id = gr.reservation_id " + //
  //           "    AND r.type = 'group' " + //
  //           "LEFT JOIN `group` g " + //
  //           "    ON gr.group_id = g.group_id " + //
  //           "LEFT JOIN match_reservation mr " + //
  //           "    ON r.reservation_id = mr.reservation_id " + //
  //           "    AND r.type = 'match' " + //
  //           "WHERE r.reservation_id = :reservationId",
  //   nativeQuery = true
  // )
  // ReservationBasicReflection getReservationDetail(@Param("reservationId") Integer reservationId);
  @Query(
    """
      SELECT NEW com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationBasicDTO(
        r.reservationId,
        v.venueId,
        v.venueName,
        c.courtId,
        c.courtName,
        t.startTime,
        t.endTime,
        r.type,
        r.state
      )
      FROM Reservation r
      JOIN r.courtAvailability ca
      JOIN ca.court c
      JOIN c.venue v
      JOIN ca.timeslot t
      WHERE r.reservationId = :reservationId
    """
  )
  ReservationBasicDTO getReservationDetail(@Param("reservationId") Integer reservationId);

  // @Query(
  //   value = "SELECT " + //
  //           "    r.reservation_id, " + //
  //           "    c.venue_id, " + //
  //           "    r.type, " + //
  //           "    t.start_time, " + //
  //           "    t.end_time, " + //
  //           "    g.group_id, " + //
  //           "    g.name " + //
  //           "FROM reservation r " + //
  //           "JOIN court_availability ca ON r.availability_id = ca.availability_id " + //
  //           "JOIN court c ON ca.court_id = c.court_id AND c.venue_id = :venueId " + //
  //           "JOIN timeslot t ON ca.timeslot_id = t.timeslot_id " + //
  //           "LEFT JOIN group_reservation gr ON r.reservation_id = gr.reservation_id " + //
  //           "LEFT JOIN `group` g ON gr.group_id = g.group_id " + //
  //           "WHERE  " + //
  //           "    (:userId IS NULL AND :userName IS NULL) " + //
  //           "    OR " + //
  //           "    EXISTS ( " + //
  //           "        SELECT 1 " + //
  //           "        FROM user_reservation ur " + //
  //           "        JOIN `user` u ON ur.user_id = u.user_id " + //
  //           "        WHERE ur.reservation_id = r.reservation_id " + //
  //           "          AND ( " + //
  //           "            (:userId IS NOT NULL AND u.user_id = :userId) " + //
  //           "            OR " + //
  //           "            (:userName IS NOT NULL AND u.user_name = :userName) " + //
  //           "          )" + //
  //           "    )",
  //   nativeQuery = true,
  //   countQuery = "SELECT COUNT(*) FROM reservation "
  // )
  // Page<ReservationManagerMetaReflection> getVenueReservationMeta(); 

    @Query("""
      SELECT NEW com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationManagerMetaDTO(
        r.reservationId,
        c.courtId,
        c.courtName,
        t.startTime, 
        t.endTime,
        r.type,
        r.state
      )
      FROM Reservation r
      JOIN r.courtAvailability ca
      JOIN ca.court c
      JOIN ca.timeslot t
      WHERE c.venueId = :venueId
    """)
    Page<ReservationManagerMetaDTO> getReservationByVenue(
        @Param("venueId") Integer venueId,
        Specification<Reservation> spec,
        Pageable pageable
    );

    @Query("SELECT NEW com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationStateCountDTO(r.state, COUNT(r)) FROM Reservation r WHERE r.availabilityId = :availabilityId GROUP BY r.state")
    List<ReservationStateCountDTO> countConflictReservation(@Param("availabilityId") Integer availabilityId);
    
    @Query("SELECT ca FROM Reservation r JOIN r.courtAvailability ca WHERE r.reservationId = :reservationId")
    CourtAvailability getReservationCourtAvailability(Integer reservationId);

}