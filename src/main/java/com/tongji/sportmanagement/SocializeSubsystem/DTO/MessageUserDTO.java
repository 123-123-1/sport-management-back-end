package com.tongji.sportmanagement.SocializeSubsystem.DTO;

import com.tongji.sportmanagement.AccountSubsystem.Entity.User;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageUserDTO {
    private Integer messageId;
    private Instant time;
    private String content;
    private Integer userId;
    private String userName;
    private String photo;

    public MessageUserDTO(Message message, User user) {
        BeanUtils.copyProperties(message, this);
        this.userName=user.getUserName();
        this.userId=user.getUserId();
        this.photo=user.getPhoto();
    }
}
