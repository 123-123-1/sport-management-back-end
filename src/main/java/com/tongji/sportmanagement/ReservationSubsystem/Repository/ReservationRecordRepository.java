package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationRecord;

@Repository
public interface ReservationRecordRepository extends JpaRepository<ReservationRecord, Integer>
{
  @Query
  List<ReservationRecord> findAllByReservationId(Integer reservationId);
}