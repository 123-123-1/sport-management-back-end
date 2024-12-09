package com.tongji.sportmanagement.Entity;


import lombok.Builder;
import lombok.Data;

@Data
public class User {
    private int id;
    private String username;
    private String password;
}
