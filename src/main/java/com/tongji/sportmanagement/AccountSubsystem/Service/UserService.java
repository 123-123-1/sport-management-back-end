package com.tongji.sportmanagement.AccountSubsystem.Service;

import com.tongji.sportmanagement.AccountSubsystem.DTO.*;
import com.tongji.sportmanagement.AccountSubsystem.Entity.Notification;
import com.tongji.sportmanagement.AccountSubsystem.Entity.NotificationState;
import com.tongji.sportmanagement.AccountSubsystem.Repository.NotificationRepository;
import com.tongji.sportmanagement.AccountSubsystem.Repository.UserRepository;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.Common.Security.JwtTokenProvider;
import com.tongji.sportmanagement.VenueSubsystem.Service.VenueService;
import com.tongji.sportmanagement.Common.OssService;
import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.DTO.ErrorMsg;
import com.tongji.sportmanagement.AccountSubsystem.Entity.User;
import com.tongji.sportmanagement.AccountSubsystem.Entity.UserType;
import com.tongji.sportmanagement.Common.DTO.UserProfileDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

//    @Autowired
//    JwtService jwtService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private OssService ossService;

    @Autowired
    private VenueService venueService; // 用于管理员注册

    public LoginResponseDTO login(String userName, String password) throws Exception {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isEmpty()) {
            throw new ServiceException(400, "用户名或密码错误");
        }
        User user = userOptional.get();
        if(!user.getPassword().equals(password)) {
            throw new ServiceException(400, "用户名或密码错误");
        }
        String userAvatar = ossService.getFileLink(getAvatarName(user.getUserId()));
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
            jwtTokenProvider.generateToken(user.getUserId()),
            jwtTokenProvider.getExpiryDate(),
            user.getUserId(),
            userName,
            userAvatar, 
            user.getUserType()
        );
        return loginResponseDTO;
    }


    public RegisterResponseDTO register(RegisterRequestDTO data) throws Exception {
        if (userRepository.findByUserName(data.getUserName()).isPresent()) {
            throw new ServiceException(422, "该用户已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(data, user);
        user.setRegistrationDate(Instant.now().plus(Duration.ofHours(8)));
        userRepository.save(user);

        // 场地管理员的注册
        if(user.getUserType() == UserType.venueadmin){
            venueService.createVenue(user.getUserId());
        }

        // 设置默认头像
        ossService.copyDefault(getAvatarName(user.getUserId()));
        return new RegisterResponseDTO(user.getUserId(), user.getUserName());
    }

    public ResponseEntity<Object> getUserList() {
        List<User> userOptional = (List<User>) userRepository.findAll();
        if(userOptional.isEmpty()) {
            return ResponseEntity.status(400).body("用户列表为空");
        }
        List<UserInfoDetailDTO> userInfoDetailDTOList = new ArrayList<>();
        for(User user : userOptional) {
            UserInfoDetailDTO userInfoDetailDTO = new UserInfoDetailDTO();
            BeanUtils.copyProperties(user, userInfoDetailDTO);
            userInfoDetailDTOList.add(userInfoDetailDTO);
        }
        return ResponseEntity.ok().body(userInfoDetailDTOList);
    }

    public ResponseEntity<Object> getUsersByName(String userName) {
        List<User> userList = (List<User>) userRepository.findUsersByName(userName);
        if(userList.isEmpty()) {
            return ResponseEntity.status(400).body(new ResultMsg("未找到用户", 0));
        }
        List<UserInfoDetailDTO> userInfoDetailDTOList = new ArrayList<>();
        for(User user : userList) {
            UserInfoDetailDTO userInfoDetailDTO = new UserInfoDetailDTO();
            BeanUtils.copyProperties(user, userInfoDetailDTO);
            userInfoDetailDTOList.add(userInfoDetailDTO);
        }
        return ResponseEntity.ok().body(userInfoDetailDTOList);
    }

    public UserInfoDetailDTO getUserInfo(int userId) throws Exception {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if(userOptional.isEmpty()) {
            throw new ServiceException(404, "未查找到该用户");
        }
        User user = userOptional.get();
        UserInfoDetailDTO userInfoDetailDTO = new UserInfoDetailDTO();
        BeanUtils.copyProperties(user, userInfoDetailDTO);
        userInfoDetailDTO.setPhoto(ossService.getFileLink(getAvatarName(userId)));
        return userInfoDetailDTO;
    }

    public UserProfileDTO getUserProfile(int userId) {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if(userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();
        BeanUtils.copyProperties(user, userProfileDTO);
        userProfileDTO.setPhoto(ossService.getFileLink(getAvatarName(userId)));
        return userProfileDTO;
    }

    public ResponseEntity<Object> updateUserInfo(int userId, UserInfoUpdateDTO data) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if(userOptional.isEmpty()) {
            return ResponseEntity.status(400).body(new ErrorMsg("未查找到该用户"));
        }
        User user = userOptional.get();
        BeanUtils.copyProperties(data, user);
        userRepository.save(user);
        UserInfoDetailDTO userInfoDetailDTO = new UserInfoDetailDTO();
        BeanUtils.copyProperties(user, userInfoDetailDTO);
        return ResponseEntity.status(200).body(userInfoDetailDTO);
    }

    public ResponseEntity<Object> updateUserPwd(int userId, UpdatePwdDTO updatePwdDTO) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if(userOptional.isEmpty()) {
            return ResponseEntity.status(400).body(new ErrorMsg("未查找到该用户"));
        }
        User user = userOptional.get();
        if(!updatePwdDTO.getOldPwd().equals(user.getPassword())) {
            return ResponseEntity.status(400).body(new ErrorMsg("用户密码错误"));
        }
        user.setPassword(updatePwdDTO.getNewPwd());
        userRepository.save(user);
        return ResponseEntity.status(200).body(new IdResponseDTO(user.getUserId()));
    }

    public ResponseEntity<Object> getUserNotification(int userId) {
        List<Notification> notificationOptional = (List<Notification>) notificationRepository.findAllByUserId(userId);
        if(notificationOptional.isEmpty()) {
            return ResponseEntity.status(400).body(new ErrorMsg("未查找到通知"));
        }
        List<NotificationDetailDTO> notificationDetailDTO = new ArrayList<>();
        for(Notification notification : notificationOptional) {
            NotificationDetailDTO dto = new NotificationDetailDTO();
            BeanUtils.copyProperties(notification, dto);
            notificationDetailDTO.add(dto);
        }
        return ResponseEntity.status(200).body(notificationDetailDTO);
    }

    public ResultMsg sendUserNotification(NotificationContentDTO notificationContentDTO) {
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationContentDTO, notification);
        notification.setTimestamp(Instant.now().plus(Duration.ofHours(8)));
        notification.setState(NotificationState.valueOf("unread"));
        notificationRepository.save(notification);
        return new ResultMsg("消息发送成功", 1);
    }

    public ResponseEntity<Object> editUserNotification(NotificationOperationDTO notificationOperationDTO) {
//        Notification notification = (Notification) notificationRepository.findByNotificationId(notificationOperationDTO.getNotificationId());
        Optional<Notification> optionalNotification = notificationRepository.findByNotificationId(notificationOperationDTO.getNotificationId());
        Notification notification = optionalNotification.get();
        notification.setState(NotificationState.valueOf(notificationOperationDTO.getOperation()));
        notificationRepository.save(notification);
        return ResponseEntity.ok().body(new ResultMsg("通知状态修改成功", 1));
    }

    public ResponseEntity<Object> updateUserAvatar(int userId, MultipartFile avatar){
        try{
            String avatarName = "avatar_" + userId;
            ossService.deleteFile(avatarName);
            ossService.uploadFile(avatar.getInputStream(), avatarName);
            return ResponseEntity.ok().body(new ResultMsg(ossService.getFileLink(avatarName), 1));
        }
        catch(IOException e){
            return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
        }
    }

    String getAvatarName(int userId){
        return "avatar_" + userId;
    }
}
