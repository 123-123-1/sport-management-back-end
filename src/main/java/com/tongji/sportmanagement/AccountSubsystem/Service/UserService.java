package com.tongji.sportmanagement.AccountSubsystem.Service;

import com.tongji.sportmanagement.AccountSubsystem.DTO.*;
import com.tongji.sportmanagement.AccountSubsystem.Repository.UserRepository;
import com.tongji.sportmanagement.Common.Security.JwtTokenProvider;
import com.tongji.sportmanagement.Common.DTO.ErrorMsg;
import com.tongji.sportmanagement.AccountSubsystem.Entity.User;
import com.tongji.sportmanagement.Common.DTO.UserProfileDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {

//    @Autowired
//    JwtService jwtService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserRepository userRepository;


    public ResponseEntity<Object> login(String userName, String password) {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorMsg("找不到该用户"));
        }
        User user = userOptional.get();
        if(!user.getPassword().equals(password)) {
            return ResponseEntity.status(400).body(new ErrorMsg("密码错误"));
        }
//        LoginResponseDTO loginResponseDTO = JwtService.getTokenById(user.getUserId());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(jwtTokenProvider.generateToken(user.getUserId()), jwtTokenProvider.getExpiryDate());
        return ResponseEntity.status(200).body(loginResponseDTO);
    }


    public ResponseEntity<Object> register(RegisterRequestDTO data) {
        if (userRepository.findByUserName(data.getUserName()).isPresent()) {
            return ResponseEntity.status(409).body(new ErrorMsg("该用户已存在"));
        }
        User user = new User();
        BeanUtils.copyProperties(data, user);
        user.setRegistrationDate(Instant.now().plus(Duration.ofHours(8)));
        userRepository.save(user);
        return ResponseEntity.status(200).body(new IdResponseDTO(user.getUserId()));
    }

    public ResponseEntity<Object> getUserInfo(int userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if(userOptional.isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorMsg("未查找到该用户"));
        }
        User user = userOptional.get();
        UserInfoDetailDTO userInfoDetailDTO = new UserInfoDetailDTO();
        BeanUtils.copyProperties(user, userInfoDetailDTO);
        return ResponseEntity.status(200).body(userInfoDetailDTO);
    }

    public UserProfileDTO getUserProfile(int userId) {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if(userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();
        BeanUtils.copyProperties(user, userProfileDTO);
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
}
