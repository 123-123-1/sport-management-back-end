package com.tongji.sportmanagement.GroupSubsystem.Service;


import com.tongji.sportmanagement.AccountSubsystem.Controller.UserController;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupMemberDetailDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.MemberDropDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.MemberQuitDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.RoleDTO;
import com.tongji.sportmanagement.GroupSubsystem.Entity.*;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupMemberRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRecordRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRepository;
import com.tongji.sportmanagement.SocializeSubsystem.Controller.SocializeController;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupRecordRepository groupRecordRepository;
    private final GroupRepository groupRepository;
    private final UserController userController;
    private final GroupRecordService groupRecordService;
    private final SocializeController socializeController;

    public GroupMemberService(GroupMemberRepository groupMemberRepository, GroupRecordRepository groupRecordRepository, GroupRepository groupRepository, UserController userController, GroupRecordService groupRecordService, SocializeController socializeController) {
        this.groupMemberRepository = groupMemberRepository;
        this.groupRecordRepository = groupRecordRepository;
        this.groupRepository = groupRepository;
        this.userController = userController;
        this.groupRecordService = groupRecordService;
        this.socializeController = socializeController;
    }


    @Transactional
    public void quitGroup(MemberQuitDTO memberQuitDTO) {
        groupMemberRepository.deleteByGroupIdAndUserId(memberQuitDTO.getGroupId(),memberQuitDTO.getMemberId());
        groupRecordRepository.deleteByGroupIdAndOperatorId(memberQuitDTO.getGroupId(),memberQuitDTO.getMemberId());
        var group=groupRepository.findById(memberQuitDTO.getGroupId()).orElseThrow();
        socializeController.quitGroupsChat(group.getChatId(), memberQuitDTO.getMemberId());
        if(groupMemberRepository.countByGroupId(memberQuitDTO.getGroupId())==0){
            groupRepository.deleteById(memberQuitDTO.getGroupId());
        }
    }

    @Transactional
    public void dropMember(MemberDropDTO memberDropDTO) {
        if (groupMemberRepository.checkAuth(memberDropDTO.getGroupId(), memberDropDTO.getOperatorId())) {
            groupMemberRepository.deleteByGroupIdAndUserId(memberDropDTO.getGroupId(), memberDropDTO.getMemberId());
            groupRecordRepository.deleteByGroupIdAndOperatorId(memberDropDTO.getGroupId(), memberDropDTO.getMemberId());
            groupRecordService.addRecord(memberDropDTO.getOperatorId(), memberDropDTO.getMemberId(),
                    memberDropDTO.getGroupId(), "将成员移出团体");
            var group=groupRepository.findById(memberDropDTO.getGroupId()).orElseThrow();
            socializeController.quitGroupsChat(group.getChatId(), memberDropDTO.getMemberId());
        }
        else{
            throw new IllegalArgumentException("没有权限将团员移出团体");
        }
    }

    @Transactional
    public void addMember(Integer groupId, Integer memberId) {
        groupMemberRepository.save(new GroupMember(null,memberId,groupId, GroupMemberRole.member));
    }

    @Transactional
    public List<GroupMemberDetailDTO> getGroupMembers(Integer groupId) {
        var members=groupMemberRepository.findGroupMembersByGroupId(groupId);
        return members.stream().map(member->{
            var memberdetail=new GroupMemberDetailDTO();
            memberdetail.setRole(member.getRole().name());
            memberdetail.setUserId(member.getUserId());
            var user=userController.getUserProfile(member.getUserId());
            memberdetail.setUserName(user.getUserName());
            memberdetail.setPhoto(user.getPhoto());
            return memberdetail;
        }).toList();
    }
    @Transactional
    public void setRole(RoleDTO roleDTO) {
        if(groupMemberRepository.checkAuth(roleDTO.getGroupId(),roleDTO.getOperatorId())){
            groupMemberRepository.updateGroupMemberByGroupIdAndUserIdAndRole(roleDTO.getGroupId(),roleDTO.getTargetId(),roleDTO.getRole().name());
            groupRecordService.addRecord(roleDTO.getOperatorId(), roleDTO.getTargetId(), roleDTO.getGroupId(),"设为管理员");
        }
        else{
            throw new IllegalArgumentException("没有权限进行该操作");
        }
    }
}
