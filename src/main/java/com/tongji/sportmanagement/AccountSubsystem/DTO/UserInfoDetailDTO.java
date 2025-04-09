package com.tongji.sportmanagement.AccountSubsystem.DTO;


import lombok.*;

import java.time.Instant;

import com.tongji.sportmanagement.AccountSubsystem.Entity.UserType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDetailDTO {
    private Integer userId;
    private String userName;
    private String phone;
    private String realName;
    private Instant registrationDate;
    private UserType userType;
    private String photo;
}
