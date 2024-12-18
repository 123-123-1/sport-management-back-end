package com.tongji.sportmanagement.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Group {
   private String name;
   private String description;
   private String groupID;
   private String photo;
   private String chatID;
}
