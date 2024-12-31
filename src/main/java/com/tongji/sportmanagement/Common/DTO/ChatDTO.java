package com.tongji.sportmanagement.Common.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatDTO {
    Integer userId;
    String chatName;
    String photo;
    private List<Integer> members;
}
