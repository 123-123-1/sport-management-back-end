package com.tongji.sportmanagement.AccountSubsystem.Repository;

import com.tongji.sportmanagement.AccountSubsystem.Entity.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Integer> {
    @Query(value = "select * from notification where user_id=:userId order by timestamp desc", nativeQuery = true)
    Iterable<Notification> findAllByUserId(int userId);
}
