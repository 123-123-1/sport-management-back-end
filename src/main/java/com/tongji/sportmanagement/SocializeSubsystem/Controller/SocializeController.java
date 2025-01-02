package com.tongji.sportmanagement.SocializeSubsystem.Controller;


import com.tongji.sportmanagement.Common.DTO.*;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.*;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.ChatType;
import com.tongji.sportmanagement.SocializeSubsystem.Service.ChatService;
import com.tongji.sportmanagement.SocializeSubsystem.Service.FriendApplicationService;
import com.tongji.sportmanagement.SocializeSubsystem.Service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/socialize")
public class SocializeController {

    private final ChatService chatService;
    private final MessageService messageService;
    private final FriendApplicationService friendApplicationService;

    public SocializeController(ChatService chatService, MessageService messageService, FriendApplicationService friendApplicationService) {
        this.chatService = chatService;
        this.messageService = messageService;
        this.friendApplicationService = friendApplicationService;
    }

    //@RequestHeader("Authorization") String token,

    @PostMapping("/chats")
    public ResponseEntity<Object> createChat( @RequestBody ChatDTO chat) {
        try {
            //验证token
            var c=chatService.createChat(chat, ChatType.groupChat);
            return ResponseEntity.status(200).body(c);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("/chats")
    public ResponseEntity<Object> getChatsByID(Integer userId){
        try{
            //验证token
            var chatList= chatService.getChatsByUserId(userId);
            return ResponseEntity.status(200).body(chatList);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @DeleteMapping("/chats")
    public ResponseEntity<Object> quitChat(@RequestBody QuitChatDTO quitChatDto){
         try{
             //验证token
             chatService.quitChat(quitChatDto.getChatId(),quitChatDto.getUserId());
             return ResponseEntity.status(200).body(ResultMsg.success("已经成功退出群聊"));
         }
         catch (Exception e){
             return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
         }
    }

    @PatchMapping("/chats")
    public ResponseEntity<Object> inviteIntoChat(@RequestBody InviteDTO inviteDto){
        try{
            //验证token
            chatService.inviteToChat(inviteDto);
            return ResponseEntity.status(200).body(ResultMsg.success("已经成功邀请该用户"));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("/chats/{chatId}")
    public ResponseEntity<Object> getChatDetail(@PathVariable Integer chatId){
        try{
            var chat=chatService.getChatDetails(chatId);
            return ResponseEntity.status(200).body(chat);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Object> sendMessage(@RequestBody MessageDTO messageDto){
        try{
            //验证token
            var message= messageService.sendMessage(messageDto);
            return ResponseEntity.status(200).body(message);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<Object> getChatHistory(Integer chatId,Integer userId) {
        try {
            //验证token
            List<MessageUserDTO> msgs=messageService.getChatHistory(chatId,userId);
            return ResponseEntity.status(200).body(msgs);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @DeleteMapping("/messages")
    public ResponseEntity<Object> deleteMessage(@RequestBody MessageDeleteDTO messageDeleteDto) {
        try{
            messageService.deleteMsg(messageDeleteDto);
            return ResponseEntity.status(200).body(ResultMsg.success("消息撤回成功"));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("/friends")
    public ResponseEntity<Object> getFriends(Integer userId){
        try {
            //验证token
            var friends= chatService.getFriendsBy(userId);
            return ResponseEntity.status(200).body(friends);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @PostMapping("/application")
    public ResponseEntity<ResultMsg> postFriendApplication(@RequestBody FriendApplicationDTO friendApplication){
        try{
            //验证token
            friendApplicationService.postFriendApplication(friendApplication);
            return ResponseEntity.status(200).body(ResultMsg.success("好友申请已发送"));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @PatchMapping("/application")
    public ResponseEntity<ResultMsg> processFriendApplication(@RequestBody AuditResultDTO auditResultDTO) {
        try {
            //验证token
            friendApplicationService.auditFriendApplication(auditResultDTO);
            return ResponseEntity.status(200).body(ResultMsg.success("好友申请处理成功"));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("/application")
    public ResponseEntity<Object> getFriendApplication(Integer userId){
        try {
            var applications=friendApplicationService.getAllFriendApplication(userId);
            return ResponseEntity.status(200).body(applications);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @DeleteMapping("/friends")
    public ResponseEntity<Object> deleteFriend(@RequestBody FriendDeleteDTO friendDeleteDTO){
        try{
            chatService.deleteFriend(friendDeleteDTO);
            return ResponseEntity.status(200).body(ResultMsg.success("好友删除成功"));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    public Integer createChatId( ChatDTO chat) {
        try {
            //验证token
            var c=chatService.createChat(chat, ChatType.groupChat);
            return c.getChatId();

        } catch (Exception e) {
            return -1;
        }
    }

    public boolean checkFriendship(Integer user1, Integer user2){
        try{
            return chatService.checkFriendship(user1,user2);
        }
        catch (Exception e){
            return false;
        }
    }

    public void quitGroupsChat(Integer chatId,Integer userId) {
        chatService.quitGroupChat(chatId,userId);
    }
}
