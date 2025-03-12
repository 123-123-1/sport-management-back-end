package com.tongji.sportmanagement.SocializeSubsystem.Controller;


import com.tongji.sportmanagement.Common.DTO.*;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.*;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.Chat;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.ChatType;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.Message;
import com.tongji.sportmanagement.SocializeSubsystem.Service.ChatService;
import com.tongji.sportmanagement.SocializeSubsystem.Service.FriendApplicationService;
import com.tongji.sportmanagement.SocializeSubsystem.Service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socialize")
public class SocializeController {

    private final ChatService chatService;
    private final MessageService messageService;
    private final FriendApplicationService friendApplicationService;

    public SocializeController(ChatService chatService, MessageService messageService, FriendApplicationService friendApplicationService) {
        this.chatService = chatService;
        this.messageService = messageService;
        this.friendApplicationService = friendApplicationService;
    }


    @PostMapping("/chats")
    public ResponseEntity<Chat> createChat(@RequestAttribute int idFromToken, @RequestBody ChatDTO chat) {
        return ResponseEntity.ok().body(chatService.createChat(chat, ChatType.friendGroup, idFromToken));
    }

    @GetMapping("/chats")
    public ResponseEntity<List<Chat>> getChatsByID(@RequestAttribute int idFromToken) {
        return ResponseEntity.ok().body(chatService.getChatsByUserId(idFromToken));
    }

    @DeleteMapping("/chats")
    public ResponseEntity<ResultMsg> quitChat(@RequestAttribute int idFromToken, Integer chatId) {
        return ResponseEntity.ok().body(chatService.quitChat(chatId,idFromToken));
    }

    @PatchMapping("/chats")
    public ResponseEntity<ResultMsg> inviteIntoChat(@RequestAttribute int idFromToken, @RequestBody InviteDTO inviteDto){
        return ResponseEntity.ok().body(chatService.inviteToChat(inviteDto, idFromToken));
    }

    @GetMapping("/chats/{chatId}")
    public ResponseEntity<ChatDetailDTO> getChatDetail(@PathVariable Integer chatId){
        return ResponseEntity.ok().body(chatService.getChatDetails(chatId));
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> sendMessage(@RequestAttribute int idFromToken, @RequestBody MessageDTO messageDto){
        return ResponseEntity.ok().body(messageService.sendMessage(messageDto, idFromToken));
    }

    @GetMapping("/messages")
    public ResponseEntity<List<MessageUserDTO>> getChatHistory(Integer chatId,@RequestAttribute int idFromToken) {
        return ResponseEntity.ok().body(messageService.getChatHistory(chatId,idFromToken));
    }

    @DeleteMapping("/messages")
    public ResponseEntity<ResultMsg> deleteMessage(@RequestAttribute int idFromToken, Integer messageId) {
        return ResponseEntity.ok().body(messageService.deleteMsg(idFromToken,messageId));
    }

    @GetMapping("/friends")
    public ResponseEntity<List<FriendDTO>> getFriends(@RequestAttribute int idFromToken){
        return ResponseEntity.ok().body(chatService.getFriendsBy(idFromToken));
    }

    @PostMapping("/application")
    public ResponseEntity<ResultMsg> postFriendApplication(@RequestAttribute int idFromToken, @RequestBody FriendApplicationDTO friendApplication){
        return ResponseEntity.ok().body(friendApplicationService.postFriendApplication(friendApplication, idFromToken));
    }

    @PatchMapping("/application")
    public ResponseEntity<ResultMsg> processFriendApplication(@RequestAttribute int idFromToken, @RequestBody AuditResultDTO auditResultDTO) throws Exception {
        return ResponseEntity.ok().body(friendApplicationService.auditFriendApplication(auditResultDTO, idFromToken));
    }

    @GetMapping("/application")
    public ResponseEntity<List<ApplicationResponseDTO>> getFriendApplication(@RequestAttribute int idFromToken) {
        return ResponseEntity.ok().body(friendApplicationService.getAllFriendApplication(idFromToken));
    }

    @DeleteMapping("/friends")
    public ResponseEntity<ResultMsg> deleteFriend(@RequestAttribute int idFromToken, @RequestBody FriendDeleteDTO friendDeleteDTO){
        return ResponseEntity.ok().body(chatService.deleteFriend(friendDeleteDTO, idFromToken));
    }

    // public Integer createChatId( ChatDTO chat,String type) {
    //     try {
    //         //验证token
    //         var c=chatService.createChat(chat, ChatType.valueOf(type));
    //         return c.getChatId();

    //     } catch (Exception e) {
    //         return -1;
    //     }
    // }
}
