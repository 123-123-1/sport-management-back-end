package com.tongji.sportmanagement.GroupSubsystem.Service;

import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupRecord;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class GroupRecordService {


    private final GroupRecordRepository groupRecordRepository;

    public GroupRecordService(GroupRecordRepository groupRecordRepository) {
        this.groupRecordRepository = groupRecordRepository;
    }

    @Transactional
    public void addRecord(Integer operator,Integer target,Integer groupId,String type) {
          GroupRecord groupRecord = new GroupRecord();
          groupRecord.setGroupId(groupId);
          groupRecord.setTime(Instant.now());
          groupRecord.setOperateType(type);
          groupRecord.setOperatorId(operator);
          groupRecord.setTargetId(target);
          groupRecordRepository.save(groupRecord);
    }

    @Transactional
    public Integer deleteRecord(Integer operator) {
        return  groupRecordRepository.deleteByOperatorId(operator);
    }

    @Transactional
    public List<GroupRecord> getRecord(Integer operator) {
        return groupRecordRepository.findGroupRecordsByOperatorId(operator);
    }
}
