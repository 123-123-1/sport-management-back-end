package com.tongji.sportmanagement.SocializeSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuitChatDTO {
    private int userId;
    private int chatId;
}
