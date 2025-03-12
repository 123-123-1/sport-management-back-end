package com.tongji.sportmanagement.SocializeSubsystem.Service;

import com.tongji.sportmanagement.AccountSubsystem.Service.UserService;
import com.tongji.sportmanagement.SocializeSubsystem.Repository.FriendApplicationRepository;
import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.DTO.AuditResultDTO;
import com.tongji.sportmanagement.Common.DTO.ChatDTO;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.Common.DTO.UserProfileDTO;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.ApplicationResponseDTO;
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
    private final UserService userService;

    public FriendApplicationService(FriendApplicationRepository friendApplicationRepository, ChatService chatService, UserService userService) {
        this.friendApplicationRepository = friendApplicationRepository;
        this.chatService = chatService;
        this.userService = userService;
    }

    @Transactional
    public ResultMsg postFriendApplication(FriendApplicationDTO application, Integer applicantId) {
        application.setApplicantId(applicantId);
        if(!friendApplicationRepository.existsByApplicantIdAndReviewerId(application.getApplicantId(),application.getReviewerId())){
            FriendApplication friendApplication = new FriendApplication();
            BeanUtils.copyProperties(application, friendApplication);
            friendApplication.setState(FriendApplicationState.waiting);
            friendApplication.setOperationTime(Instant.now());
            friendApplication.setExpirationTime(Instant.now().plus(Duration.ofDays(3)));
            friendApplicationRepository.save(friendApplication);
        }
        else{
            throw new IllegalArgumentException("无需再发送好友申请");
        }
        return ResultMsg.success("好友申请已发送");
    }
    @Transactional
    public ResultMsg auditFriendApplication(AuditResultDTO auditResultDTO, Integer reviewerId) throws Exception {
        auditResultDTO.setReviewerId(reviewerId);
        if (!friendApplicationRepository.existsByWaitingApplicationIdAndReviewerId(auditResultDTO.getAuditObjectId(), auditResultDTO.getReviewerId())) {
            throw new ServiceException(404, "找不到该用户");
        }
        if (auditResultDTO.isResult()) {
            friendApplicationRepository.setState(auditResultDTO.getAuditObjectId(),FriendApplicationState.accepted);
            Integer userId=friendApplicationRepository.getApplicantByApplicationId(auditResultDTO.getAuditObjectId());
            List<Integer> members=List.of(userId,auditResultDTO.getReviewerId());
            chatService.createChat(new ChatDTO(auditResultDTO.getReviewerId(),null,null,members), ChatType.friendChat);
        }
        else {
            friendApplicationRepository.setState(auditResultDTO.getAuditObjectId(),FriendApplicationState.rejected);
        }
        return ResultMsg.success("好友申请处理成功");
    }

    public List<ApplicationResponseDTO> getAllFriendApplication(Integer userId) {
        List<FriendApplication> applications = (List<FriendApplication>) friendApplicationRepository.findFriendApplicationsByReviewerId(userId);
        return applications.stream().map(application -> {
            ApplicationResponseDTO m = new ApplicationResponseDTO();
            m.setFriendApplicationId(application.getFriendApplicationId());
            m.setOperationTime(application.getOperationTime());
            m.setReviewerId(application.getReviewerId());
            m.setExpirationTime(application.getExpirationTime());
            m.setApplyInfo(application.getApplyInfo());
            m.setApplicantId(application.getApplicantId());
            m.setState(application.getState());
            UserProfileDTO applicant = userService.getUserProfile(application.getApplicantId());
            m.setApplicantName(applicant.getUserName());
            UserProfileDTO reviewer = userService.getUserProfile(application.getReviewerId());
            m.setReviewerName(reviewer.getUserName());
            return m;
        }).toList();
    }
}
