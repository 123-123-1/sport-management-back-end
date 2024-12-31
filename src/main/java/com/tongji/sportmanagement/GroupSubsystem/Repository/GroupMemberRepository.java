package com.tongji.sportmanagement.GroupSubsystem.Repository;

import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Integer> {

    List<GroupMember> findGroupMembersByGroupId(Integer groupId);

    @Query("select exists( select g from GroupMember g where g.userId=?2 and g.groupId=?1 and g.role='leader')")
    boolean checkAuth(Integer groupId, Integer userId);

    void deleteByGroupId(Integer groupId);

    void deleteByGroupIdAndUserId(Integer groupId, Integer memberId);

    int countByGroupId(Integer groupId);
}
