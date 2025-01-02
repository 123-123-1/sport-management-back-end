package com.tongji.sportmanagement.SocializeSubsystem.Repository;

import com.tongji.sportmanagement.SocializeSubsystem.Entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Integer> {

    Integer deleteByChatIdAndUserId(Integer chatId, Integer userId);

    @Query("select exists (select cm from  ChatMember cm where cm.chatId=?1 and  cm.userId=?2)")
    boolean existsChatMemberByChatIdAndUserId(Integer chatId, Integer userId);


    int countByChatId(Integer chatId);

    List<ChatMember> findChatMembersByChatId(Integer chatId);


    void deleteByUserIdAndChatId(Integer operatorId,Integer chatId);
}
