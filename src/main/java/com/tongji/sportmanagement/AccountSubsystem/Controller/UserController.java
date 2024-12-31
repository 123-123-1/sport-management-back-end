package com.tongji.sportmanagement.AccountSubsystem.Controller;

import com.tongji.sportmanagement.AccountSubsystem.DTO.LoginRequestDTO;
import com.tongji.sportmanagement.AccountSubsystem.DTO.RegisterRequestDTO;
import com.tongji.sportmanagement.AccountSubsystem.Service.UserService;
import com.tongji.sportmanagement.Common.DTO.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO loginRequestDTO) {
                return userService.login(loginRequestDTO.getUserName(), loginRequestDTO.getPassword());
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return userService.register(registerRequestDTO);
    }

    @PostMapping("/test")
    public String test() {
        return "test";
    }

}
