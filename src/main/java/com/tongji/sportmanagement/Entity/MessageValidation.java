package com.tongji.sportmanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "messagevaildation")
public class MessageValidation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "message_code", length = 10)
    private String messageCode;

    @Column(name = "expiration_time")
    private Instant expirationTime;

    @Column(name = "user_id", nullable = false)
    private Integer user;

}