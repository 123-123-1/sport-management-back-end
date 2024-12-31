package com.tongji.sportmanagement.Repository;


import com.tongji.sportmanagement.SocializeSubsystem.DTO.MessageUserDTO;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MessageRepository  extends JpaRepository<Message, Integer> {

    @Query("select new com.tongji.sportmanagement.SocializeSubsystem.DTO.MessageUserDTO(m,u)" +
            " from Message m join Chat c on c.chatId=m.chatId  join User u on m.userId=u.userId")
    List<MessageUserDTO> getHistoryByChatId(Integer chatId);

    @Modifying
    @Query("delete from Message m where m.messageId=?1 and m.userId=?2 and m.time>?3")
    Integer deleteByMessageIdAndUserIdAndTime(Integer messageId, Integer userId,Instant time);
}
