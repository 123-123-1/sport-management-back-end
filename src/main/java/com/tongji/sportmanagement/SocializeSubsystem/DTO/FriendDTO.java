package com.tongji.sportmanagement.SocializeSubsystem.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendDTO {
    Integer userId;
    Integer chatId;
    String userName;
    String photo;
}
