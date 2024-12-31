package com.tongji.sportmanagement.AccountSubsystem.Service;

import com.tongji.sportmanagement.AccountSubsystem.DTO.LoginResponseDTO;
import com.tongji.sportmanagement.AccountSubsystem.DTO.RegisterRequestDTO;
import com.tongji.sportmanagement.AccountSubsystem.Repository.UserRepository;
import com.tongji.sportmanagement.Common.DTO.ErrorMsg;
import com.tongji.sportmanagement.AccountSubsystem.Entity.User;
import com.tongji.sportmanagement.Common.DTO.UserProfileDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {

    JwtService jwtService;

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
        LoginResponseDTO loginResponseDTO = JwtService.getTokenById(user.getUserId());
        return ResponseEntity.status(200).body(loginResponseDTO);
    }


    public ResponseEntity<Object> register(RegisterRequestDTO data) {
        if (userRepository.findByUserName(data.getUserName()).isPresent()) {
            return ResponseEntity.status(409).body(new ErrorMsg("该用户已存在"));
        }
        User user = new User();
        BeanUtils.copyProperties(data, user);
        user.setRegistrationDate(Instant.now());
        userRepository.save(user);
        return ResponseEntity.status(200).body("success");
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
}
