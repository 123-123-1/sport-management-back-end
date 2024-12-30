package com.tongji.sportmanagement.SocializeSubsystem.Repository;


import com.tongji.sportmanagement.SocializeSubsystem.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MessageRepository  extends JpaRepository<Message, Integer> {

    boolean existsMessageByChatIdAndUserId(Integer chatId, Integer userId);

    List<Message> getAllByChatId(Integer chatId);

    @Modifying
    @Query("delete from Message m where m.messageId=?1 and m.userId=?2 and m.time>?3")
    Integer deleteByMessageIdAndUserIdAndTime(Integer messageId, Integer userId,Instant time);
}
