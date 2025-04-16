package com.tongji.sportmanagement.ReservationSubsystem.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.Violation;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ViolationState;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.ViolationRepository;

@Service
public class ViolationService
{
  @Autowired
  private ViolationRepository violationRepository;

  final Integer lockThreshold = 3; // 违约3次封禁
  final Long lockTime = 7L * 24 * 60 * 60 * 1000;

  // 检查并更新违约次数
  void checkViolationUpdate(Violation violation)
  {
    LocalDateTime updateTime = LocalDateTime.ofInstant(violation.getUpdateTime(), ZoneId.of("UTC+8"));
    LocalDateTime now = LocalDateTime.now();
    if(updateTime.getYear() != now.getYear() || updateTime.getMonth() != now.getMonth()){
      violation.setViolationCount(0);
      violation.setUpdateTime(now.toInstant(ZoneOffset.ofHours(8)));
      violationRepository.save(violation);
    }
    if(violation.getState() == ViolationState.locked){
      if(violation.getUnlockTime().isBefore(Instant.now())){
        violation.setState(ViolationState.normal);
        violation.setViolationCount(0);
        violationRepository.save(violation);
      }
    }
  }

  public Violation getUserViolation(Integer userId) throws Exception
  {
    Optional<Violation> optres = violationRepository.findByUserId(userId);
    if(optres.isEmpty()){
      throw new ServiceException(404, "未找到用户违约信息");
    }
    Violation res = optres.get();
    checkViolationUpdate(res);
    return res;
  }

  public void createViolation(Integer userId)
  {
    Violation violation = new Violation(null, ViolationState.normal, 0, null, Instant.now(), userId);
    violationRepository.save(violation);
  }

  public void violationIncrement(Integer userId) throws Exception
  {
    Optional<Violation> violationOptional = violationRepository.findByUserId(userId);
    if(violationOptional.isEmpty()){
      throw new ServiceException(404, "未找到用户违约信息");
    }
    Violation violation = violationOptional.get();
    violation.setViolationCount(violation.getViolationCount() + 1);
    if(violation.getViolationCount() >= lockThreshold) {
      Instant unlockTime = Instant.ofEpochMilli(Instant.now().toEpochMilli() + lockTime);
      violation.setUnlockTime(unlockTime);
      violation.setState(ViolationState.locked);
    }
    violation.setUpdateTime(Instant.now());
    violationRepository.save(violation);
  }

  public Boolean checkViolationState(Integer userId) throws Exception
  {
    Violation violation = getUserViolation(userId);
    if(violation.getState() == ViolationState.locked){
      return false;
    }
    return true;
  }
}
