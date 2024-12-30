package com.tongji.sportmanagement.SocializeSubsystem.Service;


import com.tongji.sportmanagement.SocializeSubsystem.DTO.ChatDto;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.Chat;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.ChatMember;
import com.tongji.sportmanagement.SocializeSubsystem.Repository.ChatMemberRepository;
import com.tongji.sportmanagement.SocializeSubsystem.Repository.ChatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {


    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatMemberRepository chatMemberRepository;

    @Transactional
    public Chat createChat(ChatDto chatDto) {
        Chat chat = new Chat();
        BeanUtils.copyProperties(chatDto, chat);
        var finalChat=chatRepository.save(chat);
        List<ChatMember> members= chatDto.getMembers().stream().map(
                userId-> {
                    ChatMember chatMember = new ChatMember();
                    chatMember.setUserId(userId);
                    chatMember.setChatId(finalChat.getChatId());
                    return chatMember;
                }
        ).toList();
        chatMemberRepository.saveAll(members);
        return chat;
    }

    public  List<Chat> getChatsByUserId(Integer userId) {
        return chatRepository.findChatsByUserId(userId);
    }

    public void quitChat(Integer chatId, Integer userId) {
        chatMemberRepository.deleteByChatIdAndUserId(chatId,userId);
    }
}
