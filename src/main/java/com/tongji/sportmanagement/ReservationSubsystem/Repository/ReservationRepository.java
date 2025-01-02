package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.Reservation;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Integer>
{
  @Query
  Optional<Reservation> findByAvailabilityId(Integer availabilityId);
}