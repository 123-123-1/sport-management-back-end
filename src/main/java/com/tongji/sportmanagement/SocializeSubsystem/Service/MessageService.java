package com.tongji.sportmanagement.SocializeSubsystem.Service;

import com.tongji.sportmanagement.AccountSubsystem.Service.UserService;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.MessageDTO;
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
    private final UserService userService;

    public MessageService(MessageRepository messageRepository, ChatMemberRepository chatMemberRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.userService = userService;
    }

    @Transactional
    public Message sendMessage(MessageDTO messageDto, Integer userId) {
        messageDto.setUserId(userId);
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
                        var user=userService.getUserProfile(message.getUserId());
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
    public ResultMsg deleteMsg(Integer userId,Integer messageId) {
        var i=messageRepository.deleteByMessageIdAndUserIdAndTime(messageId, userId,Instant.now().minus(Duration.ofMinutes(5)));
        if(i==0){
           throw new RuntimeException("撤回该信息失败");
        }
        return ResultMsg.success("消息撤回成功");
    }
}
