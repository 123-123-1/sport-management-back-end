package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationMetaDTO;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.UserReservation;

@Repository
public interface UserReservationRepository extends JpaRepository<UserReservation, Integer>
{
  @Query
  Iterable<UserReservation> findAllByUserId(Integer userId);

  // @Query(value = "SELECT * FROM user_reservation WHERE reservation_id = :reservationId", nativeQuery = true)
  // Iterable<UserReservation> findAllByReservationId(@Param("reservationId") Integer reservationId);
  // @Query(
  //   value = "SELECT " + //
  //           "    ur.user_reservation_id, " + //
  //           "    u.user_id, " + //
  //           "    u.user_name, " + //
  //           "    ur.state " + //
  //           "FROM user_reservation ur " + //
  //           "JOIN `user` u ON ur.user_id = u.user_id " + //
  //           "WHERE ur.reservation_id = :reservationId",
  //   nativeQuery = true
  // )
  // List<ReservationUserReflection> getReservationUsers(@Param("reservationId") Integer reservationId);

  @Query("SELECT ur FROM UserReservation ur WHERE ur.reservationId = :reservationId")
  List<UserReservation> findAllByReservationId(@Param("reservationId") Integer reservationId);

  @Query("""
    SELECT NEW com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationMetaDTO(
      r.reservationId,
      ur.state,
      v.venueName,
      c.courtName,
      r.type,
      t.startTime,
      t.endTime
    )
    FROM UserReservation ur
    JOIN ur.reservation r
    JOIN r.courtAvailability ca
    JOIN ca.court c
    JOIN ca.timeslot t
    JOIN c.venue v
    WHERE ur.userId = :userId
  """)
  List<ReservationMetaDTO> getUserReservationsMeta(@Param("userId")Integer userId);
}
