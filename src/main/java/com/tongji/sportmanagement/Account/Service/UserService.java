package com.tongji.sportmanagement.Account.Service;

import com.tongji.sportmanagement.DTO.ErrorMsg;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    JwtService jwtService;

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


}
