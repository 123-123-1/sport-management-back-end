package com.tongji.sportmanagement.SocializeSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MessageDTO {
    private String content;
    private Integer chatId;
    private Integer userId;
}
