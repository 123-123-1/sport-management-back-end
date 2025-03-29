package com.tongji.sportmanagement.ExternalManagementSubsystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.TimeslotConfig;

public interface TimeslotConfigRepository extends JpaRepository<TimeslotConfig, Integer>
{
  @Query
  public List<TimeslotConfig> findAllByAvconfigId(Integer avconfigId);

  @Modifying
  @Query(value = "DELETE FROM timeslot_config WHERE avconfig_id = :avconfigId AND tsconfig_id NOT IN :existId", nativeQuery = true)
  public void deleteNonexistById(@Param("avconfigId") Integer avconfigId, @Param("existId") List<Integer> existId);

  @Modifying
  @Query
  public void deleteAllByAvconfigId(Integer avconfigId);
}