package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationMetaDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationUserDTO;
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
      r.state,
      ur.userState,
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
    ORDER BY t.startTime DESC
  """)
  Page<ReservationMetaDTO> getUserReservationsMeta(@Param("userId")Integer userId, Pageable pageable);

  @Query("SELECT ur FROM UserReservation ur WHERE ur.reservationId = :reservationId AND ur.userId = :userId")
  Optional<UserReservation> findByUserIdAndReservationId(@Param("reservationId") Integer reservationId, @Param("userId") Integer userId);

  @Query("""
    SELECT NEW com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationUserDTO(
      ur.userReservationId, u.userId, u.userName, NULL, ur.userState, u.realName, u.phone
    )
    FROM UserReservation ur
    JOIN ur.user u
    WHERE ur.userReservationId IN :userReservationIdList
  """)
  List<ReservationUserDTO> getReservationUsers(@Param("userReservationIdList") List<Integer> userReservationIdList);

  @Query("""
    SELECT NEW com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationUserDTO(
      ur.userReservationId, u.userId, u.userName, NULL, ur.userState, u.realName, u.phone
    )
    FROM UserReservation ur
    JOIN ur.user u
    WHERE ur.reservationId = :reservationId
  """)
  List<ReservationUserDTO> getUsersByReservationId(@Param("reservationId") Integer reservationId);

  @Query(value = "SELECT COUNT(ur.user_id) FROM user_reservation ur WHERE ur.reservation_id = :reservationId AND ur.user_state != \"cancelled\"", nativeQuery = true)
  Long countReservationUsers(@Param("reservationId") Integer reservationId);
}
