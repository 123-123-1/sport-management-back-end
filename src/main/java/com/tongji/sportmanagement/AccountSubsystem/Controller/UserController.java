package com.tongji.sportmanagement.AccountSubsystem.Controller;

import com.tongji.sportmanagement.AccountSubsystem.DTO.LoginRequestDTO;
import com.tongji.sportmanagement.AccountSubsystem.DTO.RegisterRequestDTO;
import com.tongji.sportmanagement.AccountSubsystem.Service.UserService;
import com.tongji.sportmanagement.Common.DTO.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO loginRequestDto) {
        return userService.login(loginRequestDto.getUserName(), loginRequestDto.getPassword());
    }

    @PostMapping("/registration")
    public ResponseEntity<Object> register(@RequestBody RegisterRequestDTO registerRequestDto) {
        return userService.register(registerRequestDto);
    }

    public UserProfileDTO getUserProfile(int userId) {
        return userService.getUserProfile(userId);
    }

    @GetMapping("/test")
    public String test() {
        return "test success";
    }

    @GetMapping("/authorTest") //header中携带token才能访问
    public String authorTest(@RequestAttribute int idFromToken) {
        return "玩家" + idFromToken + ":原神，启动";
    }
}