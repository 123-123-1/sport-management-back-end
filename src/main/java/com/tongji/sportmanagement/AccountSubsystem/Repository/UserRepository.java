package com.tongji.sportmanagement.AccountSubsystem.Repository;

import com.tongji.sportmanagement.AccountSubsystem.Entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserId(Integer userId);

    @Query(value = "select * from user where user_name like %:name% ", nativeQuery = true)
    Iterable<User> findUsersByName(@Param("name") String name);
}
