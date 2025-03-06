package com.tongji.sportmanagement.GroupSubsystem.Service;

import com.tongji.sportmanagement.AccountSubsystem.Service.UserService;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupMemberDetailDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.MemberDropDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.RoleDTO;
import com.tongji.sportmanagement.GroupSubsystem.Entity.*;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupApplicationRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupMemberRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRecordRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRepository;
import com.tongji.sportmanagement.SocializeSubsystem.Service.ChatService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupRecordRepository groupRecordRepository;
    private final GroupRepository groupRepository;
    private final GroupRecordService groupRecordService;
    private final GroupApplicationRepository groupApplicationRepository;
    private final ChatService chatService;
    private final UserService userService;

    public GroupMemberService(GroupMemberRepository groupMemberRepository, GroupRecordRepository groupRecordRepository, GroupRepository groupRepository, GroupRecordService groupRecordService, GroupApplicationRepository groupApplicationRepository, UserService userService, ChatService chatService) {
        this.groupMemberRepository = groupMemberRepository;
        this.groupRecordRepository = groupRecordRepository;
        this.groupRepository = groupRepository;
        this.groupRecordService = groupRecordService;
        this.groupApplicationRepository = groupApplicationRepository;
        this.userService = userService;
        this.chatService = chatService;
    }


    @Transactional
    public void quitGroup(Integer groupId,Integer memberId) {
        if(!groupMemberRepository.existsByUserId(memberId)) {
            throw new IllegalArgumentException("该用户没有加入团体");
        }
        groupApplicationRepository.deleteByUserId(memberId);
        groupMemberRepository.deleteByGroupIdAndUserId(groupId,memberId);
        groupRecordRepository.deleteByGroupIdAndOperatorId(groupId,memberId);
        var group=groupRepository.findById(groupId).orElseThrow();
        chatService.quitGroupChat(group.getChatId(), memberId);
        if(groupMemberRepository.countByGroupId(groupId)==0){
            groupRepository.deleteById(groupId);
        }
    }

    @Transactional
    public void dropMember(MemberDropDTO memberDropDTO) {
        if(!groupMemberRepository.existsByUserId(memberDropDTO.getMemberId())){
            throw new IllegalArgumentException("该用户没有加入团体");
        }
        if (groupMemberRepository.checkAuth(memberDropDTO.getGroupId(), memberDropDTO.getOperatorId())
             && !groupMemberRepository.checkAuth(memberDropDTO.getGroupId(), memberDropDTO.getMemberId())) {
            groupApplicationRepository.deleteByUserId(memberDropDTO.getMemberId());
            groupMemberRepository.deleteByGroupIdAndUserId(memberDropDTO.getGroupId(), memberDropDTO.getMemberId());
            groupRecordRepository.deleteByGroupIdAndOperatorId(memberDropDTO.getGroupId(), memberDropDTO.getMemberId());
            groupRecordService.addRecord(memberDropDTO.getOperatorId(), memberDropDTO.getMemberId(),
                    memberDropDTO.getGroupId(), "将成员移出团体");
            var group=groupRepository.findById(memberDropDTO.getGroupId()).orElseThrow();
            chatService.quitGroupChat(group.getChatId(), memberDropDTO.getMemberId());
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
            var user=userService.getUserProfile(member.getUserId());
            memberdetail.setUserName(user.getUserName());
            memberdetail.setPhoto(user.getPhoto());
            return memberdetail;
        }).toList();
    }
    @Transactional
    public void setRole(RoleDTO roleDTO) {
        if(groupMemberRepository.checkAuth(roleDTO.getGroupId(),roleDTO.getOperatorId())){
            groupMemberRepository.updateGroupMemberByGroupIdAndUserIdAndRole(roleDTO.getGroupId(),roleDTO.getTargetId(),roleDTO.getRole());
            groupRecordService.addRecord(roleDTO.getOperatorId(), roleDTO.getTargetId(), roleDTO.getGroupId(),"设为管理员");
        }
        else{
            throw new IllegalArgumentException("没有权限进行该操作");
        }
    }
}
