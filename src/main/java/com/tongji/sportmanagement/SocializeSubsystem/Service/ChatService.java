package com.tongji.sportmanagement.SocializeSubsystem.Service;

import com.tongji.sportmanagement.AccountSubsystem.Controller.UserController;
import com.tongji.sportmanagement.Common.DTO.ChatDTO;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.ChatDetailDTO;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.FriendDTO;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.FriendDeleteDTO;
import com.tongji.sportmanagement.Common.DTO.InviteDTO;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.LittleUserDTO;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.Chat;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.ChatMember;
import com.tongji.sportmanagement.SocializeSubsystem.Repository.ChatMemberRepository;
import com.tongji.sportmanagement.SocializeSubsystem.Repository.ChatRepository;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.ChatType;
import com.tongji.sportmanagement.SocializeSubsystem.Repository.FriendApplicationRepository;
import com.tongji.sportmanagement.SocializeSubsystem.Repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final MessageRepository messageRepository;
    private final UserController userController;
    private final FriendApplicationRepository friendApplicationRepository;

    public ChatService(ChatRepository chatRepository, ChatMemberRepository chatMemberRepository, MessageRepository messageRepository, UserController userController, FriendApplicationRepository friendApplicationRepository) {
        this.chatRepository = chatRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.messageRepository = messageRepository;
        this.userController = userController;
        this.friendApplicationRepository = friendApplicationRepository;
    }

    @Transactional
    public Chat createChat(ChatDTO chatDto,ChatType chatType) {
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
        if(chatMemberRepository.countByChatId(chatId)==0){
            messageRepository.deleteByChatId(chatId);
            chatRepository.deleteById(chatId);
        }
    }

    @Transactional
    public void inviteToChat(InviteDTO inviteDto) {
        var friendShipId=chatRepository.getFriendship(inviteDto.getUserId(),inviteDto.getInviteeId());
        if(friendShipId==null){
            throw new IllegalArgumentException("两人不是好友关系");
        }
        if(!chatRepository.checkTypeGroupChat(inviteDto.getChatId())){
            throw new IllegalArgumentException("该群聊不支持邀请好友");
        }
        if(!chatMemberRepository.existsChatMemberByChatIdAndUserId(inviteDto.getChatId(),inviteDto.getUserId())){
            throw new IllegalArgumentException("并非该群聊的用户，不能邀请");
        }
        if (!chatMemberRepository.existsChatMemberByChatIdAndUserId(inviteDto.getChatId(), inviteDto.getInviteeId())) {
            ChatMember chatMember = new ChatMember();
            chatMember.setChatId(inviteDto.getChatId());
            chatMember.setUserId(inviteDto.getInviteeId());
            chatMemberRepository.save(chatMember);
        }
        else{
            throw new RuntimeException("该用户已是群聊成员");
        }
    }

    public ChatDetailDTO getChatDetails(Integer chatId) {
        Chat chat=chatRepository.findById(chatId).orElse(null);
        if(chat==null){
            throw  new RuntimeException("未找到对应的群聊");
        }
        ChatDetailDTO chatDetailDTO=new ChatDetailDTO();
        BeanUtils.copyProperties(chat,chatDetailDTO);
        var members=chatMemberRepository.findChatMembersByChatId(chatId);

        chatDetailDTO.setMembers(members.stream().map(
                member->{
                    var m=new LittleUserDTO();
                    m.setUserId(member.getUserId());
                    var p=userController.getUserProfile(member.getUserId());
                    m.setPhoto(p.getPhoto());
                    m.setUserName(p.getUserName());
                    return m;
                }
        ).toList());
        return chatDetailDTO;
    }

    @Transactional
    public List<FriendDTO> getFriendsBy(Integer userId) {
        var chats = chatRepository.findFriendsByUserId(userId);
        return chats.stream().map(
                chat->{
                    var friend=new FriendDTO();
                    BeanUtils.copyProperties(chat,friend);
                    var user=userController.getUserProfile(chat.getUserId());
                    BeanUtils.copyProperties(user,friend);
                    return friend;
                }
        ).toList();
    }

    @Transactional
    public void deleteFriend(FriendDeleteDTO ff) {
        if(chatRepository.existFriendship(ff.getOperatorId(),ff.getTargetId(),ff.getChatId())){
             chatMemberRepository.deleteByUserIdAndChatId(ff.getOperatorId(), ff.getChatId());
             chatMemberRepository.deleteByUserIdAndChatId(ff.getTargetId(), ff.getChatId());
             messageRepository.deleteByChatId(ff.getChatId());
             chatRepository.deleteById(ff.getChatId());
             friendApplicationRepository.deleteByApplicantIdAndReviewerId(ff.getOperatorId(),ff.getTargetId());
        }
        else{
            throw new IllegalArgumentException("删除的好友关系不存在");
        }
    }

    public boolean checkFriendship(Integer user1, Integer user2) {
        return chatRepository.getFriendship(user1, user2) != null;
    }

    public void quitGroupChat(Integer chatId, Integer userId) {
        var p=chatMemberRepository.deleteByChatIdAndUserId(chatId, userId);
        if(chatMemberRepository.countByChatId(chatId)==0){
            messageRepository.deleteByChatId(chatId);
            chatRepository.deleteById(chatId);
        }
    }

    @Transactional
    public void inviteIntoGroupChat(Integer chatId, Integer userId) {
        if(!chatRepository.checkTypeGroupChat(chatId)){
            throw new IllegalArgumentException("该群聊不支持邀请好友");
        }
        if (!chatMemberRepository.existsChatMemberByChatIdAndUserId(chatId, userId)) {
            ChatMember chatMember = new ChatMember();
            chatMember.setChatId(chatId);
            chatMember.setUserId(userId);
            chatMemberRepository.save(chatMember);
        }
        else{
            throw new RuntimeException("该用户已是群聊成员");
        }
    }
}
