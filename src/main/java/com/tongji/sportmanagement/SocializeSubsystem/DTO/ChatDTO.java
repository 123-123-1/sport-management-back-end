package com.tongji.sportmanagement.SocializeSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class ChatDTO {
    Integer userId;
    String chatName;
    String photo;
    private List<Integer> members;
}