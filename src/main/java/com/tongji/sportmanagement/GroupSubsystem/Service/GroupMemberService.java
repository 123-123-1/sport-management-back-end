package com.tongji.sportmanagement.GroupSubsystem.Service;


import com.tongji.sportmanagement.AccountSubsystem.Controller.UserController;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupMemberDetailDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.MemberDropDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.MemberQuitDTO;
import com.tongji.sportmanagement.GroupSubsystem.Entity.*;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupMemberRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRecordRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRepository;
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

    public GroupMemberService(GroupMemberRepository groupMemberRepository, GroupRecordRepository groupRecordRepository, GroupRepository groupRepository, UserController userController) {
        this.groupMemberRepository = groupMemberRepository;
        this.groupRecordRepository = groupRecordRepository;
        this.groupRepository = groupRepository;
        this.userController = userController;
    }


    @Transactional
    public void quitGroup(MemberQuitDTO memberQuitDTO) {
        groupMemberRepository.deleteByGroupIdAndUserId(memberQuitDTO.getGroupId(),memberQuitDTO.getMemberId());
        groupRecordRepository.deleteByGroupIdAndOperatorId(memberQuitDTO.getGroupId(),memberQuitDTO.getMemberId());
        if(groupMemberRepository.countByGroupId(memberQuitDTO.getGroupId())==0){
            groupRepository.deleteById(memberQuitDTO.getGroupId());
        }
    }

    @Transactional
    public void dropMember(MemberDropDTO memberDropDTO) {
        groupMemberRepository.deleteByGroupIdAndUserId(memberDropDTO.getGroupId(),memberDropDTO.getMemberId());
        groupRecordRepository.deleteByGroupIdAndOperatorId(memberDropDTO.getGroupId(),memberDropDTO.getMemberId());
        groupRecordRepository.save(new GroupRecord(null,memberDropDTO.getOperatorId(), memberDropDTO.getMemberId(),
                                   memberDropDTO.getGroupId(), Instant.now(),"将用户"+memberDropDTO.getMemberId().toString()+"移出团体"));
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
}
