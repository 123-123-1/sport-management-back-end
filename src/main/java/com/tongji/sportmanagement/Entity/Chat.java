package com.tongji.sportmanagement.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class Chat {
    private String chatID;
    private String chatName;
    private String type;
    private Date creatingTime;
    private String photo;
}
