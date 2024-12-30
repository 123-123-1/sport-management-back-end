package com.tongji.sportmanagement.SocializeSubsystem.Repository;


import com.tongji.sportmanagement.SocializeSubsystem.Entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository  extends JpaRepository<Chat, Integer> {

    @Query("select c from Chat c join ChatMember cm on c.chatId=cm.chatId" +
            " join User u on u.userId=cm.userId where u.userId=?1")
    List<Chat> findChatsByUserId(Integer userId);
}
