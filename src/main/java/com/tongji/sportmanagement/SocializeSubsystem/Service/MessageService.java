package com.tongji.sportmanagement.SocializeSubsystem.Service;


import com.tongji.sportmanagement.SocializeSubsystem.DTO.MessageDto;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.Message;
import com.tongji.sportmanagement.SocializeSubsystem.Repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;


    @Transactional
    public Message sendMessage(MessageDto messageDto) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDto, message);
        return messageRepository.save(message);
    }
}
