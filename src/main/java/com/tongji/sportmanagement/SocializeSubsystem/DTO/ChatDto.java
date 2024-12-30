package com.tongji.sportmanagement.SocializeSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class ChatDto {
    Integer userId;
    Integer chatId;
    String chatName;
    Instant creatingTime;
    String photo;
    private List<Integer> members;
}
