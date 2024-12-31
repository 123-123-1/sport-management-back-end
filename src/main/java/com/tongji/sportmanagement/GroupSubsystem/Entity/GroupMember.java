package com.tongji.sportmanagement.GroupSubsystem.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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

    @NotNull
    @ColumnDefault("'member'")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private GroupMemberRole state;
}

