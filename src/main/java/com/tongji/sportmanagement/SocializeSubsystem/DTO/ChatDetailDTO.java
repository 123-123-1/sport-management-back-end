package com.tongji.sportmanagement.SocializeSubsystem.DTO;


import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ChatDetailDTO {
    private Integer chatId;
    private String chatName;
    private String type;
    private Instant creatingTime;
    private String photo;
    private List<LittleUserDTO> members;
}
