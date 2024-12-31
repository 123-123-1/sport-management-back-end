package com.tongji.sportmanagement.GroupSubsystem.Service;


import com.tongji.sportmanagement.Common.DTO.ChatDTO;
import com.tongji.sportmanagement.GroupSubsystem.Controller.GroupController;
import com.tongji.sportmanagement.GroupSubsystem.DTO.CompleteGroupDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupDeleteDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupDetailDTO;
import com.tongji.sportmanagement.GroupSubsystem.Entity.Group;
import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupMember;
import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupMemberRole;
import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupRecord;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupApplicationRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupMemberRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRecordRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupController groupController;
    private final GroupApplicationService groupApplicationService;
    private final GroupRecordRepository groupRecordRepository;
    private final GroupApplicationRepository groupApplicationRepository;

    public GroupService(GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, GroupController groupController, GroupApplicationService groupApplicationService, GroupRecordRepository groupRecordRepository, GroupApplicationRepository groupApplicationRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupController = groupController;
        this.groupApplicationService = groupApplicationService;
        this.groupRecordRepository = groupRecordRepository;
        this.groupApplicationRepository = groupApplicationRepository;
    }

    @Transactional
    public void createGroup(CompleteGroupDTO completeGroup){
        Group group = new Group();
        //创建群聊
        Integer chatId=groupController.createGroupChat(new ChatDTO(completeGroup.getCreatorId(),completeGroup.getGroupName(),null,completeGroup.getMembers()));
        BeanUtils.copyProperties(completeGroup,group);
        group.setChatId(chatId);
        //创建团体
        var _group= groupRepository.save(group);
        //添加管理员
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(_group.getGroupId());
        groupMember.setUserId(completeGroup.getCreatorId());
        groupMember.setRole(GroupMemberRole.leader);
        groupMemberRepository.save(groupMember);
        //添加记录
        groupRecordRepository.save(new GroupRecord(null, completeGroup.getCreatorId(), null, _group.getGroupId(), Instant.now(),"创建团体"));
        //发送邀请
        groupApplicationService.sendApplicationsEd(completeGroup.getMembers(),completeGroup.getCreatorId(),_group);
    }

    public List<Group> getGroups(){
        return groupRepository.findAll();
    }

    @Transactional
    public GroupDetailDTO getGroupDetail(Integer groupId){
        Group group = groupRepository.findById(groupId).orElse(null);
        if(group == null){
            throw new IllegalArgumentException("找不到该团体");
        }
        GroupDetailDTO groupDetailDTO = new GroupDetailDTO();
        BeanUtils.copyProperties(group,groupDetailDTO);
        List<GroupMember> members = groupMemberRepository.findGroupMembersByGroupId(groupId);
        groupDetailDTO.setMembers(groupController.getUserDetail(members.stream().map(GroupMember::getUserId).toList()));
        return groupDetailDTO;
    }

    @Transactional
    public void deleteGroup(GroupDeleteDTO deleteDTO){
        if(groupMemberRepository.checkAuth(deleteDTO.getGroupId(),deleteDTO.getUserId())){
            groupMemberRepository.deleteByGroupId(deleteDTO.getGroupId());
            groupRecordRepository.deleteByGroupId(deleteDTO.getGroupId());
            groupApplicationRepository.deleteByGroupId(deleteDTO.getGroupId());
            groupRepository.deleteById(deleteDTO.getGroupId());
        }
        else{
            throw new IllegalArgumentException("该用户没有权限解散团体");
        }
    }
}
