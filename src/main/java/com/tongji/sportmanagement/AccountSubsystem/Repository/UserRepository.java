package com.tongji.sportmanagement.AccountSubsystem.Repository;

import com.tongji.sportmanagement.AccountSubsystem.Entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserId(Integer userId);
}
