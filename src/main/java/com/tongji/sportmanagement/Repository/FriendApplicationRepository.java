package com.tongji.sportmanagement.Repository;

import com.tongji.sportmanagement.SocializeSubsystem.Entity.FriendApplication;
import com.tongji.sportmanagement.SocializeSubsystem.Entity.FriendApplicationState;
import com.tongji.sportmanagement.UserSystem.Entity.User;
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

    @Query("select u from FriendApplication f join User u on f.applicantId=u.userId")
    User getApplicantByApplicationId(Integer applicationId);

    List<FriendApplication> findFriendApplicationsByReviewerId(Integer userId);
}
