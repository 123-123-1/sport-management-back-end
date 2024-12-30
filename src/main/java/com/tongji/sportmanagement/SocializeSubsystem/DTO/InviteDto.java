package com.tongji.sportmanagement.SocializeSubsystem.DTO;


import lombok.Data;

@Data
public class InviteDto {
    private Integer chatId;
    private Integer userId;
    private Integer inviteeId;
}
