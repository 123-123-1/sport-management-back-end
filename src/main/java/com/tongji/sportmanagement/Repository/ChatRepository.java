package com.tongji.sportmanagement.Repository;


import com.tongji.sportmanagement.Common.DTO.LittleUserDTO;
import com.tongji.sportmanagement.SocializeSubsystem.DTO.FriendDTO;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository  extends JpaRepository<Chat, Integer> {

    @Query("select c from Chat c join ChatMember cm on c.chatId=cm.chatId where cm.userId=?1 and c.type='groupChat'")
    List<Chat> findChatsByUserId(Integer userId);

    @Query("select new com.tongji.sportmanagement.Common.DTO.LittleUserDTO(u.userId,u.userName,u.photo) " +
            "from User u join ChatMember cm " +
            "on u.userId=cm.userId where cm.chatId=?1")
    List<LittleUserDTO> getLittleUserByChatId(Integer chatId);

    @Query("select new com.tongji.sportmanagement.SocializeSubsystem.DTO.FriendDTO(c.chatId, u.userId, u.userName, u.photo) " +
            "from Chat c " +
            "join ChatMember cm on c.chatId = cm.chatId " +
            "join ChatMember cmm on c.chatId = cmm.chatId " +
            "join User u on cmm.userId = u.userId " +
            "where cm.userId = ?1 and c.type = 'friendChat' " +
            "and cmm.userId != ?1")
    List<FriendDTO> findFriendsByUserId(Integer userId);
}
