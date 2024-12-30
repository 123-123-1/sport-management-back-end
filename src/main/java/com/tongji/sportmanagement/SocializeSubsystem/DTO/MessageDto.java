package com.tongji.sportmanagement.SocializeSubsystem.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
public class MessageDto {
    private Instant time;
    private String content;
    private Integer chatId;
    private Integer userId;
}
