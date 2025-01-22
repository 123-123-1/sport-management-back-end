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
}
