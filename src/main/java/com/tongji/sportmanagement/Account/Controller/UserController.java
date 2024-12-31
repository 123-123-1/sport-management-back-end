package com.tongji.sportmanagement.Account.Controller;

import com.tongji.sportmanagement.Account.DTO.LoginRequestDTO;
import com.tongji.sportmanagement.Account.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}
