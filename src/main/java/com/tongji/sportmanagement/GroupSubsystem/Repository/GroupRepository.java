package com.tongji.sportmanagement.GroupSubsystem.Repository;


import com.tongji.sportmanagement.GroupSubsystem.Entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

}
