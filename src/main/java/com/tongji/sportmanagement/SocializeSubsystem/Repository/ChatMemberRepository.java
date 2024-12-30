package com.tongji.sportmanagement.SocializeSubsystem.Repository;

import com.tongji.sportmanagement.SocializeSubsystem.Entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Integer> {

    void deleteByChatIdAndUserId(Integer chatId, Integer userId);
}
