package com.tongji.sportmanagement.AccountSubsystem.DTO;


import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDetailDTO {
    private Integer userId;
    private String userName;
    private String phone;
    private String realName;
    private Instant registrationDate;
    private String photo;
}
