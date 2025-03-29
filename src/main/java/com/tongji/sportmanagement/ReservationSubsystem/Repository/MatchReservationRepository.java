package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.MatchReservation;

@Repository
public interface MatchReservationRepository extends JpaRepository<MatchReservation, Integer>
{
  @Query
  Optional<MatchReservation> findByReservationId(Integer reservationId);
}