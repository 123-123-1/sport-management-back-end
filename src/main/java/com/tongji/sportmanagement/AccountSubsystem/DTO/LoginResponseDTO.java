package com.tongji.sportmanagement.AccountSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import com.tongji.sportmanagement.AccountSubsystem.Entity.UserType;;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private Instant expiration_time;
    private int userId;
    private String userName;
    private String userAvatar;
    private UserType userType;
}
