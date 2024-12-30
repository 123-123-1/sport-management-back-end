package com.tongji.sportmanagement.Common.Repository;

import com.tongji.sportmanagement.Common.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
