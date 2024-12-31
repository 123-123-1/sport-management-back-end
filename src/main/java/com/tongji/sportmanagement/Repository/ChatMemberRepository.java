package com.tongji.sportmanagement.Repository;

import com.tongji.sportmanagement.SocializeSubsystem.Entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Integer> {

    Integer deleteByChatIdAndUserId(Integer chatId, Integer userId);

    boolean existsChatMemberByChatIdAndUserId(Integer chatId, Integer userId);

}
