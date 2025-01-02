package com.tongji.sportmanagement.GroupSubsystem.Service;

import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupRecord;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupMemberRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class GroupRecordService {


    private final GroupRecordRepository groupRecordRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupRecordService(GroupRecordRepository groupRecordRepository, GroupMemberRepository groupMemberRepository) {
        this.groupRecordRepository = groupRecordRepository;
        this.groupMemberRepository = groupMemberRepository;
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
    public List<GroupRecord> getRecord(Integer operator,Integer targetId, Integer groupId) {
        if(groupMemberRepository.checkAuth(groupId,operator)) {
            return groupRecordRepository.findRecords(targetId, groupId);
        }
        else{
            throw  new IllegalArgumentException("没有权限查看团员记录");
        }
    }

}
