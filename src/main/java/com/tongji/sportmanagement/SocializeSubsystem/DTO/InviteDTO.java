package com.tongji.sportmanagement.SocializeSubsystem.DTO;


import lombok.Data;

@Data
public class InviteDTO {
    private Integer chatId;
    private Integer userId;
    private Integer inviteeId;
}
