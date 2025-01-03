package com.tongji.sportmanagement.AccountSubsystem.Controller;

import com.tongji.sportmanagement.AccountSubsystem.DTO.*;
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

    @GetMapping("/list")
    public ResponseEntity<Object> getUserList() {
        return userService.getUserList();
    }

    @GetMapping("/names")
    public ResponseEntity<Object> getUserByName(@RequestParam String userName) {
        return userService.getUsersByName(userName);
    }

    @GetMapping("/info")
    public ResponseEntity<Object> getUserInfo(@RequestAttribute int idFromToken) {
        return userService.getUserInfo(idFromToken);
    }

    @PatchMapping("/info")
    public ResponseEntity<Object> updateUserInfo(@RequestAttribute int idFromToken, @RequestBody UserInfoUpdateDTO userInfoUpdateDTO) {
        return userService.updateUserInfo(idFromToken, userInfoUpdateDTO);
    }

    @PatchMapping("/password")
    public ResponseEntity<Object> updateUserPassword(@RequestAttribute int idFromToken, @RequestBody UpdatePwdDTO updatePwdDTO) {
        return userService.updateUserPwd(idFromToken, updatePwdDTO);
    }

    @GetMapping("/notifications")
    public ResponseEntity<Object> getUserNotifications(@RequestAttribute int idFromToken) {
        return userService.getUserNotification(idFromToken);
    }

    @PostMapping("/newNotifications")
    public ResponseEntity<Object> sendUserNotifications(@RequestBody NotificationContentDTO notificationContentDTO) {
        System.out.println(notificationContentDTO.toString());
        return userService.sendUserNotification(notificationContentDTO);
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
        return "玩家" + idFromToken + ": 原神，启动!";
    }
}