package com.tongji.sportmanagement.Controller;


import com.tongji.sportmanagement.DTO.*;
import com.tongji.sportmanagement.Entity.VenueComment;
import com.tongji.sportmanagement.Entity.FriendApplication;
import com.tongji.sportmanagement.Entity.Message;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tongji.sportmanagement.Entity.Chat;

import java.util.*;

@RestController
@RequestMapping("/api/socialize")
public class SocializeController {


//    @GetMapping("/getChatByID")
//    public ResponseEntity<ArrayList<Chat>> getChatByID(String userID){
//        ArrayList<Chat> chatList = new ArrayList<>();
//        chatList.add(new Chat("100","100","100",new Date(),null));
//        return ResponseEntity.status(200).body(chatList);
//    }

    @PostMapping("/createChat")
    public ResponseEntity<ResultMsg> createChat(String userID, @RequestBody CompleteChat chat){
        return ResponseEntity.status(200).body(new ResultMsg(userID+chat.toString()));
    }

    @DeleteMapping("/quitChat")
    public ResponseEntity<ResultMsg> deleteChat(String chatID,String userID){
        return ResponseEntity.status(200).body(new ResultMsg(chatID+userID));
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<ResultMsg> sendMessage(String chatID , @RequestBody Message message){
        return ResponseEntity.status(200).body(new ResultMsg(chatID+message.toString()));
    }

    @GetMapping("/getChatHistory")
    public ResponseEntity<ArrayList<ExpandMessage>> getChatHistory(String chatID){
        var p=new ArrayList<ExpandMessage>();
        p.add( new ExpandMessage(chatID,null,null,null,null,null));
        return ResponseEntity.status(200).body(p);
    }

    @DeleteMapping("/deleteMessage")
    public ResponseEntity<ResultMsg> deleteMessage(String msgID){
        return ResponseEntity.status(200).body(new ResultMsg(msgID));
    }

    @GetMapping("/getChatMembers")
    public ResponseEntity<ArrayList<BriefUser>> getChatMember(String chatID){
        var p=new ArrayList<BriefUser>();
        p.add( new BriefUser(chatID,null,null));
        return ResponseEntity.status(200).body(p);
    }

//    @GetMapping("/getFriends")
//    public ResponseEntity<ArrayList<Chat>> getFirends(String userID){
//        var p=new ArrayList<Chat>();
//        p.add( new Chat(userID,null,null,null,null));
//        return ResponseEntity.status(200).body(p);
//    }

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

//    @GetMapping("/getFriendApplication")
//    public ResponseEntity<ArrayList<FriendApplication>> getFriendApplication(String userID){
//        var p=new ArrayList<FriendApplication>();
//        p.add(new FriendApplication(userID,null,null,null,null,null,null));
//        return ResponseEntity.status(200).body(p);
//    }

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
    }
}
