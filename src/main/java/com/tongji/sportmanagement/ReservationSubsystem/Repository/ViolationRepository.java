package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.Violation;

@Repository
public interface ViolationRepository extends CrudRepository<Violation, Integer>
{
  @Query
  Optional<Violation> findByUserId(Integer userId);
}
