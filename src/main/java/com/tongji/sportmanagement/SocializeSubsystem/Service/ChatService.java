package com.tongji.sportmanagement.SocializeSubsystem.Service;


import com.tongji.sportmanagement.Repository.UserRepository;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.ChatDTO;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.ChatDetailDTO;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.FriendDTO;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.InviteDTO;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.Chat;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.ChatMember;
import com.tongji.sportmanagement.Repository.ChatMemberRepository;
import com.tongji.sportmanagement.Repository.ChatRepository;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.ChatType;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;

    public ChatService(ChatRepository chatRepository, ChatMemberRepository chatMemberRepository,UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.chatMemberRepository = chatMemberRepository;
    }

    @Transactional
    public Chat createChat(ChatDTO chatDto,ChatType chatType) {
        if(!chatDto.getMembers().contains(chatDto.getUserId())){
            throw new RuntimeException("群聊成员不包含发起人");
        }
        Chat chat = new Chat();
        BeanUtils.copyProperties(chatDto, chat);
        chat.setType(chatType);
        chat.setCreatingTime(Instant.now());
        var finalChat = chatRepository.save(chat);
        List<ChatMember> members = chatDto.getMembers().stream().map(
                userId -> {
                    ChatMember chatMember = new ChatMember();
                    chatMember.setUserId(userId);
                    chatMember.setChatId(finalChat.getChatId());
                    return chatMember;
                }
        ).toList();
        chatMemberRepository.saveAll(members);
        return chat;
    }


    public List<Chat> getChatsByUserId(Integer userId) {
        return chatRepository.findChatsByUserId(userId);
    }

    @Transactional
    public void quitChat(Integer chatId, Integer userId) {
        var p=chatMemberRepository.deleteByChatIdAndUserId(chatId, userId);
        if(p==0){
            throw new RuntimeException("该用户没有加入该群聊");
        }
    }

    @Transactional
    public void inviteToChat(InviteDTO inviteDto) {
        if (chatMemberRepository.existsChatMemberByChatIdAndUserId(inviteDto.getChatId(), inviteDto.getUserId())) {
            ChatMember chatMember = new ChatMember();
            chatMember.setChatId(inviteDto.getChatId());
            chatMember.setUserId(inviteDto.getInviteeId());
            chatMemberRepository.save(chatMember);
        }
        else{
            throw new RuntimeException("该用户并非群聊成员");
        }
    }

    public ChatDetailDTO getChatDetails(Integer chatId) {
        Chat chat=chatRepository.findById(chatId).orElse(null);
        if(chat==null){
            throw  new RuntimeException("未找到对应的群聊");
        }
        ChatDetailDTO chatDetailDTO=new ChatDetailDTO();
        BeanUtils.copyProperties(chat,chatDetailDTO);
        chatDetailDTO.setMembers(chatRepository.getLittleUserByChatId(chatId));
        return chatDetailDTO;
    }

    public List<FriendDTO> getFriendsBy(Integer userId) {
        return chatRepository.findFriendsByUserId(userId);
    }
}
