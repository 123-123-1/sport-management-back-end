package com.tongji.sportmanagement.GroupSubsystem.Repository;

import com.tongji.sportmanagement.GroupSubsystem.Entity.GroupApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupApplicationRepository extends JpaRepository<GroupApplication, Integer> {

    List<GroupApplication> findAllByReviewerId(Integer userId);

    void deleteByGroupId(Integer groupId);

    @Query("select g from GroupApplication g join GroupMember  gm on g.groupId=gm.groupId and gm.role='leader' and gm.userId=?1 and g.type='apply'")
    List<GroupApplication> findAllByGroup(Integer userId);


    @Modifying
    @Query("delete GroupApplication g where (g.type='apply' and g.applicantId=?1) or (g.type='invited' and g.reviewerId=?1)")
    void deleteByUserId(Integer memberId);
}
