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

    boolean existsByApplicantIdAndReviewerId(Integer auditObjectId, Integer reviewerId);


    @Modifying
    @Query("update FriendApplication f set f.state=?2 where f.applicantId=?1")
    void setState(Integer applicationId, FriendApplicationState friendApplicationState);

    @Query("select f.applicantId from FriendApplication f where f.applicantId=?1")
    Integer getApplicantByApplicationId(Integer applicationId);

    List<FriendApplication> findFriendApplicationsByReviewerId(Integer userId);
}
