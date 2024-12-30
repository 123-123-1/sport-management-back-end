package com.tongji.sportmanagement.SocializeSubsystem.DTO;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.Instant;

@Data
public class MessageUserDto {
    private Integer messageId;
    private Instant time;
    private String content;
    private Integer userId;
    private String userName;
    private String photo;
}
