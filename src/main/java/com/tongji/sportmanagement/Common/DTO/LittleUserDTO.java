package com.tongji.sportmanagement.Common.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LittleUserDTO {
    private int userId;
    private String userName;
    private String photo;
}
