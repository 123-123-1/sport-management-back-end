package com.tongji.sportmanagement.Account.Controller;

import com.tongji.sportmanagement.Account.DTO.LoginRequestDTO;
import com.tongji.sportmanagement.Account.DTO.RegisterRequestDTO;
import com.tongji.sportmanagement.Account.Service.UserService;
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
