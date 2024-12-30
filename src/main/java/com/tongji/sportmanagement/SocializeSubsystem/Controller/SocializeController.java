package com.tongji.sportmanagement.SocializeSubsystem.Controller;


import com.tongji.sportmanagement.Common.DTO.ResultData;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;

import com.tongji.sportmanagement.SocializeSubsystem.DTO.*;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.Message;
import com.tongji.sportmanagement.SocializeSubsystem.Service.ChatService;
import com.tongji.sportmanagement.SocializeSubsystem.Service.MessageService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socialize")
public class SocializeController {

    private final ChatService chatService;
    private final MessageService messageService;

    public SocializeController(ChatService chatService, MessageService messageService) {
        this.chatService = chatService;
        this.messageService = messageService;
    }

    //@RequestHeader("Authorization") String token,

    @PostMapping("/Chat")
    public ResponseEntity<Object> createChat( @RequestBody ChatDto chat) {
        try {
            //验证token
            var c=chatService.createChat(chat);
            return ResponseEntity.status(200).body(ResultData.success(c));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("/Chat")
    public ResponseEntity<Object> getChatsByID(@RequestBody  Integer userId){
        try{
            //验证token
            var chatList= chatService.getChatsByUserId(userId);
            return ResponseEntity.status(200).body(ResultData.success(chatList));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @DeleteMapping("/Chat")
    public ResponseEntity<Object> quitChat(@RequestBody QuitChatDto quitChatDto){
         try{
             //验证token
             chatService.quitChat(quitChatDto.getChatId(),quitChatDto.getUserId());
             return ResponseEntity.status(200).body(ResultMsg.success("已经成功退出群聊"));
         }
         catch (Exception e){
             return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
         }
    }

    @PatchMapping("/Chat")
    public ResponseEntity<Object> inviteIntoChat(@RequestBody InviteDto inviteDto){
        try{
            //验证token
            chatService.inviteToChat(inviteDto);
            return ResponseEntity.status(200).body(ResultMsg.success("已经成功邀请该用户"));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @PostMapping("/Message")
    public ResponseEntity<Object> sendMessage(@RequestBody MessageDto messageDto){
        try{
            //验证token
            var message= messageService.sendMessage(messageDto);
            return ResponseEntity.status(200).body(ResultData.success(message));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("/Message")
    public ResponseEntity<Object> getChatHistory(@RequestBody MessageHistoryDto messageHistoryDto) {
        try {
            //验证token
            List<Message> msgs=messageService.getChatHistory(messageHistoryDto);
            return ResponseEntity.status(200).body(ResultData.success(msgs));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @DeleteMapping("/Message")
    public ResponseEntity<ResultMsg> deleteMessage(@RequestBody MessageDeleteDto messageDeleteDto) {
        try{
            messageService.deleteMsg(messageDeleteDto);
            return ResponseEntity.status(200).body(ResultMsg.success("消息撤回成功"));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }
/*
    @GetMapping("/getChatMembers")
    public ResponseEntity<ArrayList<BriefUser>> getChatMember(String chatID){
        var p=new ArrayList<BriefUser>();
        p.add( new BriefUser(chatID,null,null));
        return ResponseEntity.status(200).body(p);
    }

    @GetMapping("/getFriends")
    public ResponseEntity<ArrayList<Chat>> getFirends(String userID){
        var p=new ArrayList<Chat>();
        p.add( new Chat(userID,null,null,null,null));
        return ResponseEntity.status(200).body(p);
    }

    @DeleteMapping("/deleteFriend")
    public ResponseEntity<ResultMsg> deleteFriend(String userID,String chatID){
        return ResponseEntity.status(200).body(new ResultMsg(userID+chatID));
    }

    @PostMapping("/postFriendApplication")
    public ResponseEntity<ResultMsg> postFriendApplication(String userID , @RequestBody FriendApplication friendApplication){
         return ResponseEntity.status(200).body(new ResultMsg(userID+friendApplication.toString()));
    }

    @PutMapping("/processFriendApplication")
    public ResponseEntity<ResultMsg> processFriendApplication(String applicationID , boolean result){
        return ResponseEntity.status(200).body(new ResultMsg(applicationID+result));
    }

    @GetMapping("/getFriendApplication")
    public ResponseEntity<ArrayList<FriendApplication>> getFriendApplication(String userID){
        var p=new ArrayList<FriendApplication>();
        p.add(new FriendApplication(userID,null,null,null,null,null,null));
        return ResponseEntity.status(200).body(p);
    }

    @GetMapping("/getVenueComments")
    public ResponseEntity<ArrayList<ExpandComment>> getVenueComments(String userID){
        var p=new ArrayList<ExpandComment>();
        p.add(new ExpandComment(userID,null,null,null,null,0,null,null));
        return ResponseEntity.status(200).body(p);
    }

    @PostMapping("/postVenueComment")
    public ResponseEntity<ResultMsg> postVenueComment(@RequestBody VenueComment comment){
        return ResponseEntity.status(200).body(new ResultMsg(comment.toString()));
    }

    @DeleteMapping("/deleteVenueComment")
    public ResponseEntity<ResultMsg> deleteVenueComment(String commentID){
        return ResponseEntity.status(200).body(new ResultMsg(commentID));
    }

    @PutMapping("/editVenueComment")
    public ResponseEntity<ResultMsg> editVenueComment(@RequestBody VenueComment comment){
        return ResponseEntity.status(200).body(new ResultMsg(comment.toString()));
    }*/
}
