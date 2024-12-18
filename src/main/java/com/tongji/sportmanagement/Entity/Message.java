package com.tongji.sportmanagement.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Message {
    private String msgID;
    private String content;
    private Date time;
    private String speakerID;
}
