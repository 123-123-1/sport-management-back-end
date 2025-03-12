package com.tongji.sportmanagement.GroupSubsystem.Service;

import com.tongji.sportmanagement.AccountSubsystem.Service.UserService;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupMemberDetailDTO;
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
    public ResultMsg quitGroup(Integer groupId,Integer memberId) {
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
        return ResultMsg.success("退出团体成功");
    }

    @Transactional
    public ResultMsg dropMember(Integer groupId, Integer memberId, Integer operatorId) {
        if(!groupMemberRepository.existsByUserId(memberId)){
            throw new IllegalArgumentException("该用户没有加入团体");
        }
        if (!groupMemberRepository.checkAuth(groupId, operatorId)
             || groupMemberRepository.checkAuth(groupId, memberId)) {
                throw new IllegalArgumentException("没有权限将团员移出团体");
        }
        groupApplicationRepository.deleteByUserId(memberId);
        groupMemberRepository.deleteByGroupIdAndUserId(groupId, memberId);
        groupRecordRepository.deleteByGroupIdAndOperatorId(groupId, memberId);
        groupRecordService.addRecord(operatorId, memberId, groupId, "将成员移出团体");
        var group=groupRepository.findById(groupId).orElseThrow();
        chatService.quitGroupChat(group.getChatId(), memberId);
        return ResultMsg.success("将用户移出团体成功");
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
    public ResultMsg setRole(RoleDTO roleDTO, Integer operatorId) {
        roleDTO.setOperatorId(operatorId);
        
        if(!groupMemberRepository.checkAuth(roleDTO.getGroupId(),roleDTO.getOperatorId())){
            throw new IllegalArgumentException("没有权限进行该操作");
        }
        groupMemberRepository.updateGroupMemberByGroupIdAndUserIdAndRole(roleDTO.getGroupId(),roleDTO.getTargetId(),roleDTO.getRole());
        groupRecordService.addRecord(roleDTO.getOperatorId(), roleDTO.getTargetId(), roleDTO.getGroupId(),"设为管理员");
        return ResultMsg.success("已成功设置用户权限");
    }
}
