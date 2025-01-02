package com.tongji.sportmanagement.GroupSubsystem.Repository;

import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRecordRepository extends JpaRepository<GroupRecord, Integer> {

    void deleteByGroupId(Integer groupId);

    void deleteByGroupIdAndOperatorId(Integer groupId, Integer memberId);

    void deleteByOperatorId(Integer operator);

    @Query("select r from GroupRecord r where r.groupId=?2 and (r.operatorId=?1 or r.targetId=?1)")
    List<GroupRecord> findRecords(Integer targetId, Integer groupId);
}
