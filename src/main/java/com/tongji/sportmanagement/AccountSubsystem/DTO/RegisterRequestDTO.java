package com.tongji.sportmanagement.AccountSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    String userName;
    String password;
    String phone;
    String realName;
    String photo;
}
