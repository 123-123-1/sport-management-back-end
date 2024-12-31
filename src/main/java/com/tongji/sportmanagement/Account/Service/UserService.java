package com.tongji.sportmanagement.Account.Service;

import com.tongji.sportmanagement.Account.DTO.RegisterRequestDTO;
import com.tongji.sportmanagement.Account.Repository.UserRepository;
import com.tongji.sportmanagement.Common.DTO.ErrorMsg;
import com.tongji.sportmanagement.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class UserService {

    JwtService jwtService;

    @Autowired
    public UserRepository userRepository;

    public ResponseEntity<Object> login(String userName, String password) {
//        try {
//            return ResponseEntity.ok(accountService.login(loginRequestDto.getIdentity(), loginRequestDto.getPassword()));
//        } catch (InvalidCredentialsException e) {
//            return ResponseEntity.status(401).body(e.getMessage());
//        }

//        try{
//            if (userName == null || password == null) {
//                throw new InvalidCredentialsException("User not found or invalid password");
//            }
//            return ResponseEntity.status(200).body("success");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body();
//        }


        if(true){
            return ResponseEntity.status(200).body("success");
        }
        else {
            return ResponseEntity.status(500).body(new ErrorMsg());
        }

    }


    public ResponseEntity<Object> register(RegisterRequestDTO data) {
        User user = new User(data.getUserName(),data.getPassword(),data.getPhone(),data.getRealName(), LocalDateTime.now(),data.getPhoto());
        userRepository.save(user);
        return ResponseEntity.status(200).body("success");
    }
}
