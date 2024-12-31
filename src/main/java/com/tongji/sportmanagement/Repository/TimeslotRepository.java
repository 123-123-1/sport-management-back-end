package com.tongji.sportmanagement.Repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.Entity.Timeslot;

@Repository
public interface TimeslotRepository extends CrudRepository<Timeslot, Integer>
{
  @Query(value = "SELECT * FROM timeslot WHERE start_time BETWEEN :start_date AND :end_date", nativeQuery = true)
  Iterable<Timeslot> findByDate(@Param("start_date") Instant start_date, @Param("end_date") Instant end_date);
}