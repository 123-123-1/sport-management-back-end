package com.tongji.sportmanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpandGroupMember {
    private String userID;
    private String userName;
    private String photo;
    private String role;
}
