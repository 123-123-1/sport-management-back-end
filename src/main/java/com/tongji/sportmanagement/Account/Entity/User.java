package com.tongji.sportmanagement.Account.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

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