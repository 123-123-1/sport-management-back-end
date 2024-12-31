package com.tongji.sportmanagement.GroupSubsystem.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "group_member")
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_member_id", nullable = false)
    private Integer groupMembershipId;

    @Column(name = "user_id")
    private Integer user;

    @Column(name = "group_id")
    private Integer group;

    @Column(name = "role")
    private Integer role;

}