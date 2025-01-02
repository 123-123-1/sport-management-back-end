package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationRecord;

@Repository
public interface ReservationRecordRepository extends CrudRepository<ReservationRecord, Integer>
{

}