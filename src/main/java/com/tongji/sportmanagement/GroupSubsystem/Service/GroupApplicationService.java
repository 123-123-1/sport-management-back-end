package com.tongji.sportmanagement.GroupSubsystem.Service;

import com.tongji.sportmanagement.AccountSubsystem.Service.UserService;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupApplicationDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupApplicationResultDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.InviteGroupDTO;
import com.tongji.sportmanagement.GroupSubsystem.Entity.*;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupApplicationRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupMemberRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRecordRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRepository;
import com.tongji.sportmanagement.Common.DTO.AuditResultDTO;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.SocializeSubsystem.Service.ChatService;
import com.tongji.sportmanagement.Common.DTO.UserProfileDTO;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class GroupApplicationService {

    private final GroupApplicationRepository groupApplicationRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRecordRepository groupRecordRepository;
    private final GroupMemberService groupMemberService;
    private final GroupRecordService groupRecordService;
    private final ChatService chatService;
    private final UserService userService;

    public GroupApplicationService(GroupApplicationRepository groupApplicationRepository, GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, GroupRecordRepository groupRecordRepository, GroupMemberService groupMemberService, GroupRecordService groupRecordService, UserService userService, ChatService chatService) {
        this.groupApplicationRepository = groupApplicationRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupRecordRepository = groupRecordRepository;
        this.groupMemberService = groupMemberService;
        this.groupRecordService = groupRecordService;
        this.userService = userService;
        this.chatService = chatService;
    }

    @Transactional
    public void sendApplicationsEd(List<Integer> targets, Integer userId, Group group) {
        List<GroupApplication> applications = targets.stream().map(
                target->{
                    GroupApplication application = new GroupApplication();
                    application.setGroupId(group.getGroupId());
                    application.setType(GroupApplicationType.invited);
                    application.setState(GroupApplicationState.waiting);
                    application.setOperationTime(Instant.now());
                    application.setExpirationTime(Instant.now().plus(Duration.ofDays(3)));
                    application.setApplicantId(userId);
                    application.setReviewerId(target);

                    application.setApplyInfo("用户“"+userService.getUserProfile(userId).getUserName()+"”邀请你加入团体“"+group.getGroupName()+"”");
                    groupRecordService.addRecord(userId,target,group.getGroupId(),"邀请加入团体");
                    return application;
                }
        ).toList();
        groupApplicationRepository.saveAll(applications);
    }

    @Transactional
    public List<GroupApplicationResultDTO> getGroupApplications(Integer userId) {
        var applications1= groupApplicationRepository.findAllByReviewerId(userId);
        var applications2= groupApplicationRepository.findAllByGroup(userId);
        List<GroupApplication> applications = Stream.concat(applications1.stream(), applications2.stream())
                .collect(Collectors.toList());
        // var applications = groupApplicationRepository.findAllByGroup(userId);
        return applications.stream().map(application -> {
            GroupApplicationResultDTO m = new GroupApplicationResultDTO();
            m.setGroupApplicationId(application.getGroupApplicationId());
            m.setOperationTime(application.getOperationTime());
            m.setReviewerId(application.getReviewerId());
            m.setExpirationTime(application.getExpirationTime());
            m.setApplyInfo(application.getApplyInfo());
            m.setApplicantId(application.getApplicantId());
            m.setState(application.getState());
            UserProfileDTO applicant = userService.getUserProfile(application.getApplicantId());
            m.setApplicantName(applicant.getUserName());
            if(application.getReviewerId() != null){
                UserProfileDTO reviewer = userService.getUserProfile(application.getReviewerId());
                m.setReviewerName(reviewer.getUserName());
            }
            Optional<Group> group = groupRepository.findById(application.getGroupId());
            m.setGroupId(application.getGroupId());
            m.setGroupName(group.get().getGroupName());
            return m;
        }).toList();
    }

    @Transactional
    public ResultMsg sendApplicationIng(GroupApplicationDTO groupApplicationDTO, Integer applicantId) {
        groupApplicationDTO.setApplicantId(applicantId);
        GroupApplication groupApplication = new GroupApplication();
        BeanUtils.copyProperties(groupApplicationDTO, groupApplication);
        groupApplication.setType(GroupApplicationType.apply);
        groupApplication.setOperationTime(Instant.now());
        groupApplication.setExpirationTime(Instant.now().plus(Duration.ofDays(3)));
        groupApplication.setState(GroupApplicationState.waiting);
        groupApplicationRepository.save(groupApplication);
        return ResultMsg.success("已经发送团体加入申请");
    }

    @Transactional
    public ResultMsg updateApplication(AuditResultDTO auditResultDTO, Integer reviewerId) {
        auditResultDTO.setReviewerId(reviewerId);
        var application = groupApplicationRepository.findById(auditResultDTO.getAuditObjectId()).orElse(null);
        if (application == null) {
            throw new IllegalArgumentException("未找到该申请");
        }
        if (application.getExpirationTime().isBefore(Instant.now())) {
            throw new RuntimeException("申请已过期");
        }
        if (!application.getState().equals(GroupApplicationState.waiting)) {
            throw new RuntimeException("该申请已经处理过了");
        }
        if (application.getType().equals(GroupApplicationType.apply) && !groupMemberRepository.checkAuth(application.getGroupId(), auditResultDTO.getReviewerId())) {
            throw new IllegalArgumentException("没有权限审核该申请");
        } else if (application.getType().equals(GroupApplicationType.invited) && !application.getReviewerId().equals(auditResultDTO.getReviewerId())) {
            throw new IllegalArgumentException("该申请的审核者有误");
        }
        var group = groupRepository.findById(application.getGroupId()).orElse(null);
        if (group == null) {
            throw new IllegalArgumentException("要加入的团体不存在");
        }
        if (auditResultDTO.isResult()) {
            application.setState(GroupApplicationState.accepted);
            application.setReviewerId(auditResultDTO.getReviewerId());
            groupApplicationRepository.save(application);

            if (application.getType().equals(GroupApplicationType.apply)) {
                groupMemberService.addMember(application.getGroupId(), application.getApplicantId());
                chatService.inviteIntoGroupChat(application.getApplicantId(),group.getChatId());
                
                groupRecordService.addRecord(application.getReviewerId(),application.getApplicantId() , group.getGroupId(),"同意加入申请");
                groupRecordService.addRecord(application.getApplicantId(),  null, group.getGroupId(), "申请加入团体");
            }
            else if (application.getType().equals(GroupApplicationType.invited)) {
                groupMemberService.addMember(application.getGroupId(), application.getReviewerId());
                chatService.inviteIntoGroupChat(application.getReviewerId(),group.getChatId());
                groupRecordService.addRecord(application.getReviewerId(), null, group.getGroupId(),"受邀加入团体");
            }
        } else {
            application.setState(GroupApplicationState.rejected);
            application.setReviewerId(auditResultDTO.getReviewerId());
            groupApplicationRepository.save(application);
            if (application.getType().equals(GroupApplicationType.apply)) {
                groupRecordService.addRecord(application.getReviewerId(), application.getApplicantId(), group.getGroupId(), "拒绝加入申请");
            }
        }
        return ResultMsg.success("已经成功处理申请");
    }

    @Transactional
    public ResultMsg inviteMember(InviteGroupDTO inviteDTO,Integer invitor) {
        if(!groupMemberRepository.checkAuth(inviteDTO.getGroupId(),invitor)&& chatService.checkFriendship(invitor,inviteDTO.getInviteeId())){
            throw new IllegalArgumentException("没有权限邀请用户加入该团体");
        }
        String info="用户"+ userService.getUserProfile(invitor).getUserName() +"邀请你加入团体"+groupRepository.findById(inviteDTO.getGroupId()).orElseThrow().getGroupName();
        groupApplicationRepository.save(new GroupApplication(null,GroupApplicationType.invited,info,GroupApplicationState.waiting,Instant.now(),Instant.now().plus(Duration.ofDays(3)),invitor,inviteDTO.getGroupId(),inviteDTO.getInviteeId()));
        groupRecordRepository.save(new GroupRecord(null,invitor,inviteDTO.getInviteeId(),inviteDTO.getGroupId(),Instant.now(),"邀请好友加入"));
        return ResultMsg.success("已经向该用户发送邀请");
    }

}
