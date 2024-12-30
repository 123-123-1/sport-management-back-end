package com.tongji.sportmanagement.SocializeSubsystem.Repository;


import com.tongji.sportmanagement.SocializeSubsystem.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository  extends JpaRepository<Message, Integer> {
}
