package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.UserReservation;

@Repository
public interface UserReservationRepository extends CrudRepository<UserReservation, Integer>
{
  
}
