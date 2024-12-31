package com.tongji.sportmanagement.GroupSubsystem.Repository;

import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRecordRepository extends JpaRepository<GroupRecord, Integer> {

    void deleteByGroupId(Integer groupId);
}
