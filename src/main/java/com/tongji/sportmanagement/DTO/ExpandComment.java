package com.tongji.sportmanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ExpandComment {
    private String commentID;
    private String venueID;
    private String userID;
    private String content;
    private Date time;
    private float score;
    private String commentName;
    private String photo;
}
