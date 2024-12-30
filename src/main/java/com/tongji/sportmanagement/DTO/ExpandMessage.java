package com.tongji.sportmanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data

public class ExpandMessage {
    private String msgID;
    private String content;
    private Date time;
    private String speakerID;
    private String speakerName;
    private String photo;
}
