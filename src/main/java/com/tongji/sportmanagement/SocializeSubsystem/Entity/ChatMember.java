package com.tongji.sportmanagement.SocializeSubsystem.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "chat_member")
public class ChatMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_member_id", nullable = false)
    private Integer chatMembershipId;

    @NonNull
    @Column(name = "chat_id")
    private Integer chatId;

    @NonNull
    @Column(name = "user_id")
    private Integer userId;

}