package com.tongji.sportmanagement.SocializeSubsystem.Service;


import com.tongji.sportmanagement.SocializeSubsystem.DTO.MessageDeleteDto;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.MessageDto;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.MessageHistoryDto;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.Message;
import com.tongji.sportmanagement.SocializeSubsystem.Repository.ChatMemberRepository;
import com.tongji.sportmanagement.SocializeSubsystem.Repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatMemberRepository chatMemberRepository;


    @Transactional
    public Message sendMessage(MessageDto messageDto) {
        if (chatMemberRepository.existsChatMemberByChatIdAndUserId(messageDto.getChatId(), messageDto.getUserId())) {
            Message message = new Message();
            BeanUtils.copyProperties(messageDto, message);
            return messageRepository.save(message);
        }
        else{
            throw new RuntimeException("该用户并非该群聊的成员");
        }
    }

    public List<Message> getChatHistory(MessageHistoryDto messageHistoryDto) {
        if(chatMemberRepository.existsChatMemberByChatIdAndUserId(messageHistoryDto.getChatId(), messageHistoryDto.getUserId())){
            return messageRepository.getAllByChatId(messageHistoryDto.getChatId());
        }
        else{
            throw new RuntimeException(("该用户并非该群聊的成员"));
        }
    }

    public void deleteMsg(MessageDeleteDto messageDeleteDto) {
        var i=messageRepository.deleteByMessageIdAndUserIdAndTime(messageDeleteDto.getMessageId(), messageDeleteDto.getUserId(),Instant.now().minus(Duration.ofMinutes(5)));
        if(i==0){
           throw new RuntimeException("撤回该信息失败");
        }
    }
}
