package com.tongji.sportmanagement.GroupSubsystem.Service;

import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupApplicationDTO;
import com.tongji.sportmanagement.GroupSubsystem.Entity.Group;
import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupApplication;
import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupApplicationState;
import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupApplicationType;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupApplicationRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupMemberRepository;
import com.tongji.sportmanagement.GroupSubsystem.Repository.GroupRepository;
import com.tongji.sportmanagement.Common.DTO.AuditResultDTO;
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

    public GroupApplicationService(GroupApplicationRepository groupApplicationRepository, GroupRepository groupRepository, GroupMemberRepository groupMemberRepository) {
        this.groupApplicationRepository = groupApplicationRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
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
                    application.setApplyInfo("用户"+target.toString()+"邀请你加入团体“"+group.getGroupName()+"”");
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

    public void updateApplication(AuditResultDTO auditResultDTO) {
        var application=groupApplicationRepository.findById(auditResultDTO.getAuditObjectId()).orElse(null);
        if(application==null){
            throw new IllegalArgumentException("未找到该申请");
        }
        if(auditResultDTO.isResult()){

        }
    }
}
