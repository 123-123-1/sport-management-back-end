package com.tongji.sportmanagement.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;


@Data
@AllArgsConstructor
public class VenueComment {
    private String commentID;
    private String venueID;
    private String userID;
    private String content;
    private Date time;
    private float score;
}
