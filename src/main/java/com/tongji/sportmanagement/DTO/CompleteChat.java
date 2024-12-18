package com.tongji.sportmanagement.DTO;

import com.tongji.sportmanagement.Entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class CompleteChat {
    private Chat chat;
    private ArrayList<String> members;
}
