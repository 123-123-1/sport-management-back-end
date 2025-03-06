package com.tongji.sportmanagement.AccountSubsystem.Controller;

import com.tongji.sportmanagement.AccountSubsystem.DTO.*;
import com.tongji.sportmanagement.AccountSubsystem.Service.UserService;
import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.DTO.ErrorMsg;

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
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO loginRequestDto) {
        try{
            return ResponseEntity.ok().body(userService.login(loginRequestDto.getUserName(), loginRequestDto.getPassword()));
        }
        catch(ServiceException e){
            return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<Object> register(@RequestBody RegisterRequestDTO registerRequestDto) {
        try{
            return ResponseEntity.ok().body(userService.register(registerRequestDto));
        }
        catch(ServiceException e){
            return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getUserList() {
        try{
            return ResponseEntity.ok().body(userService.getUserList());
        }
        catch(ServiceException e){
            return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
        }
    }

    @GetMapping("/names")
    public ResponseEntity<Object> getUserByName(@RequestParam String userName) {
        try{
            return ResponseEntity.ok().body(userService.getUsersByName(userName));
        }
        catch(ServiceException e){
            return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Object> getUserInfo(@RequestAttribute int idFromToken) {
        try{
            return ResponseEntity.ok().body(userService.getUserInfo(idFromToken));
        }
        catch(ServiceException e){
            return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
        }
    }

    @PatchMapping("/info")
    public ResponseEntity<Object> updateUserInfo(@RequestAttribute int idFromToken, @RequestBody UserInfoUpdateDTO userInfoUpdateDTO) {
        try{
            return ResponseEntity.ok().body(userService.updateUserInfo(idFromToken, userInfoUpdateDTO));
        }
        catch(ServiceException e){
            return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<Object> updateUserPassword(@RequestAttribute int idFromToken, @RequestBody UpdatePwdDTO updatePwdDTO) {
        try{
            return ResponseEntity.ok().body(userService.updateUserPwd(idFromToken, updatePwdDTO));
        }
        catch(ServiceException e){
            return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
        }
    }

    @PostMapping("/avatar")
    public ResponseEntity<Object> updateUserAvatar(@RequestAttribute int idFromToken, @RequestParam("avatar") MultipartFile avatar){
        try{
            return ResponseEntity.ok().body(userService.updateUserAvatar(idFromToken, avatar));
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
        }
    }

    @GetMapping("/notifications")
    public ResponseEntity<Object> getUserNotification(@RequestAttribute int idFromToken) {
        try{
            return ResponseEntity.ok().body(userService.getUserNotification(idFromToken));
        }
        catch(ServiceException e){
            return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
        }
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