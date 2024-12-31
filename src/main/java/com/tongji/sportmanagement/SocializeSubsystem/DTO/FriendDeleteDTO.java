package com.tongji.sportmanagement.SocializeSubsystem.DTO;


import lombok.Data;

@Data
public class FriendDeleteDTO {
    Integer operatorId;
    Integer targetId;
    Integer chatId;
}
