package com.tongji.sportmanagement.GroupSubsystem.Service;

import com.tongji.sportmanagement.AccountSubsystem.Controller.UserController;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupApplicationDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.InviteGroupDTO;
import com.tongji.sportmanagement.GroupSubsystem.Entity.*;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupApplicationRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupMemberRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRecordRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRepository;
import com.tongji.sportmanagement.Common.DTO.AuditResultDTO;
import com.tongji.sportmanagement.SocializeSubsystem.Controller.SocializeController;
import com.tongji.sportmanagement.Common.DTO.InviteDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class GroupApplicationService {


    private final GroupApplicationRepository groupApplicationRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final SocializeController socializeController;
    private final GroupRecordRepository groupRecordRepository;
    private final GroupMemberService groupMemberService;
    private final UserController userController;
    private final GroupRecordService groupRecordService;

    public GroupApplicationService(GroupApplicationRepository groupApplicationRepository, GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, SocializeController socializeController, GroupRecordRepository groupRecordRepository, GroupMemberService groupMemberService, UserController userController, GroupRecordService groupRecordService) {
        this.groupApplicationRepository = groupApplicationRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.socializeController = socializeController;
        this.groupRecordRepository = groupRecordRepository;
        this.groupMemberService = groupMemberService;
        this.userController = userController;
        this.groupRecordService = groupRecordService;
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

                    application.setApplyInfo("用户“"+userController.getUserProfile(userId).getUserName()+"”邀请你加入团体“"+group.getGroupName()+"”");
                    groupRecordService.addRecord(userId,target,group.getGroupId(),"邀请加入团体");
                    return application;
                }
        ).toList();
        groupApplicationRepository.saveAll(applications);
    }

    @Transactional
    public List<GroupApplication> getGroupApplications(Integer userId) {
        var applications1= groupApplicationRepository.findAllByReviewerId(userId);
        var applications2= groupApplicationRepository.findAllByGroup(userId);
        return Stream.concat(applications1.stream(), applications2.stream())
                .collect(Collectors.toList());
    }

    @Transactional
    public void sendApplicationIng(GroupApplicationDTO groupApplicationDTO) {
        GroupApplication groupApplication = new GroupApplication();
        BeanUtils.copyProperties(groupApplicationDTO, groupApplication);
        groupApplication.setType(GroupApplicationType.apply);
        groupApplication.setOperationTime(Instant.now());
        groupApplication.setExpirationTime(Instant.now().plus(Duration.ofDays(3)));
        groupApplication.setState(GroupApplicationState.waiting);
        groupApplicationRepository.save(groupApplication);
    }

    @Transactional
    public void updateApplication(AuditResultDTO auditResultDTO) {
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
                socializeController.inviteIntoChat(new InviteDTO(group.getChatId(), auditResultDTO.getReviewerId(), application.getApplicantId()));
                groupRecordService.addRecord(application.getReviewerId(),application.getApplicantId() , group.getGroupId(),"同意加入申请");
                groupRecordService.addRecord(application.getApplicantId(),  null, group.getGroupId(), "申请加入团体");
            }
            else if (application.getType().equals(GroupApplicationType.invited)) {
                groupMemberService.addMember(application.getGroupId(), application.getReviewerId());
                socializeController.inviteIntoChat(new InviteDTO(group.getChatId(), application.getApplicantId(), application.getReviewerId()));
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
    }

    @Transactional
    public void inviteMember(InviteGroupDTO inviteDTO,Integer invitor) {
        if(groupMemberRepository.checkAuth(inviteDTO.getGroupId(),invitor)&& socializeController.checkFriendship(invitor,inviteDTO.getInviteeId())){
            String info="用户"+ userController.getUserProfile(invitor).getUserName() +"邀请你加入团体"+groupRepository.findById(inviteDTO.getGroupId()).orElseThrow().getGroupName();
            groupApplicationRepository.save(new GroupApplication(null,GroupApplicationType.invited,info,GroupApplicationState.waiting,Instant.now(),Instant.now().plus(Duration.ofDays(3)),invitor,inviteDTO.getGroupId(),inviteDTO.getInviteeId()));
            groupRecordRepository.save(new GroupRecord(null,invitor,inviteDTO.getInviteeId(),inviteDTO.getGroupId(),Instant.now(),"邀请好友加入"));
        }
        else{
            throw new IllegalArgumentException("没有权限邀请用户加入该团体");
        }
    }

}
