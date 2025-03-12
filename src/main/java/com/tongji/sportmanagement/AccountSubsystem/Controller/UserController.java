package com.tongji.sportmanagement.AccountSubsystem.Controller;

import com.tongji.sportmanagement.AccountSubsystem.DTO.*;
import com.tongji.sportmanagement.AccountSubsystem.Service.UserService;
import com.tongji.sportmanagement.Common.DTO.ErrorMsg;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDto) throws Exception {
        return ResponseEntity.ok().body(userService.login(loginRequestDto.getUserName(), loginRequestDto.getPassword()));
    }

    @PostMapping("/registration")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDto) throws Exception{
        return ResponseEntity.ok().body(userService.register(registerRequestDto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserInfoDetailDTO>> getUserList() throws Exception {
        return ResponseEntity.ok().body(userService.getUserList());
    }

    @GetMapping("/names")
    public ResponseEntity<List<UserInfoDetailDTO>> getUserByName(@RequestParam String userName) throws Exception {
        return ResponseEntity.ok().body(userService.getUsersByName(userName));
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoDetailDTO> getUserInfo(@RequestAttribute int idFromToken) throws Exception {
        return ResponseEntity.ok().body(userService.getUserInfo(idFromToken));
    }

    @PatchMapping("/info")
    public ResponseEntity<UserInfoDetailDTO> updateUserInfo(@RequestAttribute int idFromToken, @RequestBody UserInfoUpdateDTO userInfoUpdateDTO) throws Exception {
        return ResponseEntity.ok().body(userService.updateUserInfo(idFromToken, userInfoUpdateDTO));
    }

    @PatchMapping("/password")
    public ResponseEntity<IdResponseDTO> updateUserPassword(@RequestAttribute int idFromToken, @RequestBody UpdatePwdDTO updatePwdDTO) throws Exception {
        return ResponseEntity.ok().body(userService.updateUserPwd(idFromToken, updatePwdDTO));
    }

    @PostMapping("/avatar")
    public ResponseEntity<ResultMsg> updateUserAvatar(@RequestAttribute int idFromToken, @RequestParam("avatar") MultipartFile avatar) throws Exception{
        return ResponseEntity.ok().body(userService.updateUserAvatar(idFromToken, avatar));
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationDetailDTO>> getUserNotification(@RequestAttribute int idFromToken) throws Exception {
        return ResponseEntity.ok().body(userService.getUserNotification(idFromToken));
    }

    @PatchMapping("/newNotifications")
    public ResponseEntity<Object> editUserNotification(@RequestBody NotificationOperationDTO notificationOperationDTO) {
        return userService.editUserNotification(notificationOperationDTO);
    }

    @PostMapping("/newNotifications")
    public ResponseEntity<Object> sendUserNotification(@RequestBody NotificationContentDTO notificationContentDTO) {
        try{
            return ResponseEntity.ok().body(userService.sendUserNotification(notificationContentDTO));
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
        }
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