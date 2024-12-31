package com.tongji.sportmanagement.Common.Repository;

import com.tongji.sportmanagement.Common.DTO.LittleUserDTO;
import com.tongji.sportmanagement.Common.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

}
