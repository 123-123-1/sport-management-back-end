package com.tongji.sportmanagement.SocializeSubsystem.Repository;


import com.tongji.sportmanagement.SocializeSubsystem.DTO.LittleUserDTO;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.FriendDTO;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.Chat;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository  extends JpaRepository<Chat, Integer> {

    @Query("select c from Chat c join ChatMember cm on c.chatId=cm.chatId where cm.userId=?1 and c.type !='friendChat'")
    List<Chat> findChatsByUserId(Integer userId);

   

    @Query("select cmm " +
            "from Chat c " +
            "join ChatMember cm on c.chatId = cm.chatId and cm.userId=?1 and c.type='friendChat'" +
            "join ChatMember cmm on c.chatId = cmm.chatId and cmm.userId!=?1")
    List<ChatMember> findFriendsByUserId(Integer userId);

    @Query("select exists(select c from Chat c where  c.chatId=?3 and c.type='friendChat') and exists(select cm2 from ChatMember cm1 join ChatMember cm2 on cm1.chatId=cm2.chatId   where cm1.chatId=?3 and cm1.userId=?1 and cm2.userId=?2)")
    boolean existFriendship(Integer userId, Integer friendId,Integer chatId);

    @Query("SELECT  not exists (select c from Chat c where c.chatId=?1 and  c.type !='friendChat')")
    boolean checkTypeGroupChat(Integer chatId);

    @Query("select c from Chat c join ChatMember cm1  on (c.chatId= cm1.chatId and cm1.userId=?1) join ChatMember cm2 on (cm1.chatId=cm2.chatId and cm2.userId=?2) where c.type='friendChat'")
    Chat getFriendship(Integer userId, Integer inviteeId);
}
