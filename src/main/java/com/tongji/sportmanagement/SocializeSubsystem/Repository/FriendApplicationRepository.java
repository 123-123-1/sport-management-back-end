package com.tongji.sportmanagement.SocializeSubsystem.Repository;

import com.tongji.sportmanagement.AccountSubsystem.Entity.User;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.FriendApplication;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.FriendApplicationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendApplicationRepository extends JpaRepository<FriendApplication, Integer> {

    @Query("select exists (select a from FriendApplication a where " +
            "(a.applicantId= ?1 and  a.reviewerId=?2 and (a.state='waiting' or a.state='accepted')) or" +
            "(a.applicantId= ?2 and  a.reviewerId=?1 and (a.state='waiting' or a.state='accepted')))")
    boolean existsByApplicantIdAndReviewerId(Integer applicantId, Integer reviewerId);


    @Modifying
    @Query("update FriendApplication f set f.state=?2 where f.friendApplicationId=?1")
    void setState(Integer applicationId, FriendApplicationState friendApplicationState);

    @Query("select f.applicantId from FriendApplication f where f.friendApplicationId=?1")
    Integer getApplicantByApplicationId(Integer applicationId);

    List<FriendApplication> findFriendApplicationsByReviewerId(Integer userId);

    @Query("select exists (select a from FriendApplication a where a.friendApplicationId=?1 and  a.reviewerId=?2 and a.state='waiting')")
    boolean existsByWaitingApplicationIdAndReviewerId(Integer applicantId, Integer reviewerId);

    @Modifying
    @Query("delete from FriendApplication f where (f.reviewerId=?1 and f.applicantId=?2) or (f.applicantId=?1 and f.reviewerId=?2)")
    void deleteByApplicantIdAndReviewerId(Integer operatorId, Integer targetId);
}
