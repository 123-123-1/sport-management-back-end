package com.tongji.sportmanagement.SocializeSubsystem.Service;


import com.tongji.sportmanagement.AccountSubsystem.Entity.User;
import com.tongji.sportmanagement.SocializeSubsystem.Repository.FriendApplicationRepository;

import com.tongji.sportmanagement.SocializeSubsystem.DTO.AuditResultDTO;
import com.tongji.sportmanagement.Common.DTO.ChatDTO;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.FriendApplicationDTO;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.ChatType;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.FriendApplication;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.FriendApplicationState;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class FriendApplicationService {

    private final FriendApplicationRepository friendApplicationRepository;
    private final ChatService chatService;

    public FriendApplicationService(FriendApplicationRepository friendApplicationRepository, ChatService chatService) {
        this.friendApplicationRepository = friendApplicationRepository;
        this.chatService = chatService;
    }

    public void postFriendApplication(FriendApplicationDTO application) {
          FriendApplication friendApplication = new FriendApplication();
          BeanUtils.copyProperties(application,friendApplication);
          friendApplication.setState(FriendApplicationState.waiting);
          friendApplication.setOperationTime(Instant.now());
          friendApplication.setExpirationTime(Instant.now().plus(Duration.ofDays(3)));
          friendApplicationRepository.save(friendApplication);
    }
    @Transactional
    public void auditFriendApplication(AuditResultDTO auditResultDTO) {
        if (friendApplicationRepository.existsByApplicantIdAndReviewerId(auditResultDTO.getAuditObjectId(), auditResultDTO.getReviewerId())) {
            if (auditResultDTO.isResult()) {
                 friendApplicationRepository.setState(auditResultDTO.getAuditObjectId(),FriendApplicationState.accepted);
                 User user=friendApplicationRepository.getApplicantByApplicationId(auditResultDTO.getAuditObjectId());
                 List<Integer> members=List.of(user.getUserId(),auditResultDTO.getReviewerId());
                 chatService.createChat(new ChatDTO(auditResultDTO.getReviewerId(),null,null,members), ChatType.friendChat);
            }
            else {
                 friendApplicationRepository.setState(auditResultDTO.getAuditObjectId(),FriendApplicationState.rejected);
            }
        }
        else{
            throw new IllegalArgumentException("找不到该好友申请");
        }
    }

    public List<FriendApplication> getAllFriendApplication(Integer userId) {
        return friendApplicationRepository.findFriendApplicationsByReviewerId(userId);
    }
}
