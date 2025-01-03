package com.tongji.sportmanagement.GroupSubsystem.Service;


import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.DTO.ChatDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.CompleteGroupDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupDetailDTO;
import com.tongji.sportmanagement.GroupSubsystem.Entity.Group;
import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupMember;
import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupMemberRole;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupApplicationRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupMemberRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRecordRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRepository;
import com.tongji.sportmanagement.SocializeSubsystem.Controller.SocializeController;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupApplicationService groupApplicationService;
    private final GroupRecordRepository groupRecordRepository;
    private final GroupApplicationRepository groupApplicationRepository;
    private final GroupRecordService groupRecordService;
    private final SocializeController socializeController;
    private final GroupMemberService groupMemberService;

    public GroupService(GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, GroupApplicationService groupApplicationService, GroupRecordRepository groupRecordRepository, GroupApplicationRepository groupApplicationRepository, GroupRecordService groupRecordService, SocializeController socializeController, GroupMemberService groupMemberService) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupApplicationService = groupApplicationService;
        this.groupRecordRepository = groupRecordRepository;
        this.groupApplicationRepository = groupApplicationRepository;
        this.groupRecordService = groupRecordService;
        this.socializeController = socializeController;
        this.groupMemberService = groupMemberService;
    }

    @Transactional
    public void createGroup(CompleteGroupDTO completeGroup){
        Group group = new Group();
        //创建群聊
        var chatId=socializeController.createChatId(new ChatDTO(completeGroup.getCreatorId(),completeGroup.getGroupName(),null,List.of(completeGroup.getCreatorId())),"groupChat");
        BeanUtils.copyProperties(completeGroup,group);
        group.setChatId(chatId);
        group.setCreationTime(Instant.now());
        //创建团体
        var _group= groupRepository.save(group);
        //添加管理员
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(_group.getGroupId());
        groupMember.setUserId(completeGroup.getCreatorId());
        groupMember.setRole(GroupMemberRole.leader);
        groupMemberRepository.save(groupMember);
        //添加记录
        groupRecordService.addRecord(completeGroup.getCreatorId(), null, _group.getGroupId(),"创建团体");
        //发送邀请
        groupApplicationService.sendApplicationsEd(completeGroup.getMembers(),completeGroup.getCreatorId(),_group);
    }

    public List<Group> getGroups(){
        return groupRepository.findAll();
    }

    public List<Group> getByUserId(Integer userId) throws Exception{
        List<GroupMember> groupMembers = groupMemberRepository.findGroupMembersByUserId(userId);
        List<Group> result = new ArrayList<Group>();
        for(GroupMember groupMember: groupMembers){
            Optional<Group> group = groupRepository.findById(groupMember.getGroupId());
            if(group.isEmpty()){
                throw new ServiceException(404, "未找到团体");
            }
            result.add(group.get());
        }
        return result;
    }

    @Transactional
    public GroupDetailDTO getGroupDetail(Integer groupId){
        Group group = groupRepository.findById(groupId).orElse(null);
        if(group == null){
            throw new IllegalArgumentException("找不到该团体");
        }
        GroupDetailDTO groupDetailDTO = new GroupDetailDTO();
        BeanUtils.copyProperties(group,groupDetailDTO);
        groupDetailDTO.setMembers(groupMemberService.getGroupMembers(groupId));
        return groupDetailDTO;
    }

    @Transactional
    public void deleteGroup(Integer groupId,Integer userId){
        if(groupMemberRepository.checkAuth(groupId,userId)){
            groupMemberRepository.deleteByGroupId(groupId);
            groupRecordRepository.deleteByGroupId(groupId);
            groupApplicationRepository.deleteByGroupId(groupId);
            groupRepository.deleteById(groupId);
        }
        else{
            throw new IllegalArgumentException("该用户没有权限解散团体");
        }
    }

    @Transactional
    public List<Group> getGroupByName(String groupName) {
        return groupRepository.findByName(groupName);
    }
}
