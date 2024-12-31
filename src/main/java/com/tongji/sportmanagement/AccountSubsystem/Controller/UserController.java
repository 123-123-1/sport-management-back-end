package com.tongji.sportmanagement.AccountSubsystem.Controller;

import com.tongji.sportmanagement.AccountSubsystem.DTO.LoginRequestDTO;
import com.tongji.sportmanagement.AccountSubsystem.Service.UserService;
import com.tongji.sportmanagement.Common.DTO.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO loginRequestDto) {
        return userService.login(loginRequestDto.getUserName(), loginRequestDto.getPassword());
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody LoginRequestDTO loginRequestDto) {
        return userService.login(loginRequestDto.getUserName(), loginRequestDto.getPassword());
    }

    public UserProfileDTO getUserProfile(int userId) {
        return userService.getUserProfile(userId);
    }
}