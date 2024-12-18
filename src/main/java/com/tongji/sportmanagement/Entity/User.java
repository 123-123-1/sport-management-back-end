package com.tongji.sportmanagement.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class User {
    private String userID;
    private String userName;
    private String password;
    private String phone;
    private String realName;
    private Date registrationDate;
    private String photo;
}
