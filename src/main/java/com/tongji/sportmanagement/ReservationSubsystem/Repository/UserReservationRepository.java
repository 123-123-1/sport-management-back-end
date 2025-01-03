package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.UserReservation;

@Repository
public interface UserReservationRepository extends CrudRepository<UserReservation, Integer>
{
  @Query
  Iterable<UserReservation> findAllByUserId(Integer userId);

  @Query(value = "SELECT * FROM user_reservation WHERE reservation_id = :reservationId", nativeQuery = true)
  Iterable<UserReservation> findAllByReservationId(@Param("reservationId") Integer reservationId);
}
