package com.tongji.sportmanagement.SocializeSubsystem.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LittleUserDTO {
    private int userId;
    private String userName;
    private String photo;
}
