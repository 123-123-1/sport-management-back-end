package com.tongji.sportmanagement.Common.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteDTO {
    private Integer chatId;
    private Integer userId;
    private Integer inviteeId;
}
