package com.tongji.sportmanagement.Common.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer userId;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "real_name", length = 100)
    private String realName;

    @Column(name = "registration_date")
    private Instant registrationDate;

    @Column(name = "photo", length = 100)
    private String photo;

}