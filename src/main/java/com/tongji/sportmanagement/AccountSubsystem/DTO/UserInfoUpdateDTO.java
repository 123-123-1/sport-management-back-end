package com.tongji.sportmanagement.AccountSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoUpdateDTO {
    String userName;
    String phone;
    String photo;
    String realName;
}
