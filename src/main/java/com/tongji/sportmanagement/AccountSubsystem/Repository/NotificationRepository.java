package com.tongji.sportmanagement.AccountSubsystem.Repository;

import com.tongji.sportmanagement.AccountSubsystem.Entity.Notification;
import com.tongji.sportmanagement.AccountSubsystem.Entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends CrudRepository<Notification, Integer> {
    Iterable<Notification> findAllByUserId(int userId);
}
