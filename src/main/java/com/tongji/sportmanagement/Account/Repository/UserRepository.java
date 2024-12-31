package com.tongji.sportmanagement.Account.Repository;

import com.tongji.sportmanagement.Account.Entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

}
