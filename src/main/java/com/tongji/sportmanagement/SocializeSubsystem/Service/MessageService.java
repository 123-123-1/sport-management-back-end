package com.tongji.sportmanagement.SocializeSubsystem.Service;


import com.tongji.sportmanagement.AccountSubsystem.Controller.UserController;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.MessageDTO;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.MessageDeleteDTO;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.MessageUserDTO;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.Message;
import com.tongji.sportmanagement.SocializeSubsystem.Repository.ChatMemberRepository;
import com.tongji.sportmanagement.SocializeSubsystem.Repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final UserController userController;

    public MessageService(MessageRepository messageRepository, ChatMemberRepository chatMemberRepository, UserController userController) {
        this.messageRepository = messageRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.userController = userController;
    }

    @Transactional
    public Message sendMessage(MessageDTO messageDto) {
        if (chatMemberRepository.existsChatMemberByChatIdAndUserId(messageDto.getChatId(), messageDto.getUserId())) {
            Message message = new Message();
            BeanUtils.copyProperties(messageDto, message);
            message.setTime(Instant.now());
            return messageRepository.save(message);
        }
        else{
            throw new RuntimeException("该用户并非该群聊的成员");
        }
    }

    public List<MessageUserDTO> getChatHistory(Integer chatId, Integer userId) {
        if(chatMemberRepository.existsChatMemberByChatIdAndUserId(chatId,userId)){
            var messages= messageRepository.getHistoryByChatId(chatId);
            return messages.stream().map(
                    message->{
                        var newmessage=new MessageUserDTO();
                        BeanUtils.copyProperties(message,newmessage);
                        var user=userController.getUserProfile(message.getUserId());
                        BeanUtils.copyProperties(user,newmessage);
                        return newmessage;
                    }
            ).toList();
        }
        else{
            throw new RuntimeException(("该用户并非该群聊的成员"));
        }
    }

    @Transactional
    public void deleteMsg(MessageDeleteDTO messageDeleteDto) {
        var i=messageRepository.deleteByMessageIdAndUserIdAndTime(messageDeleteDto.getMessageId(), messageDeleteDto.getUserId(),Instant.now().minus(Duration.ofMinutes(5)));
        if(i==0){
           throw new RuntimeException("撤回该信息失败");
        }
    }
}
