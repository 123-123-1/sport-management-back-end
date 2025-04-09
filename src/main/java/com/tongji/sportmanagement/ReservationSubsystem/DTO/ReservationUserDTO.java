package com.tongji.sportmanagement.ReservationSubsystem.DTO;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationUserState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUserDTO
{
  Integer userReservationId;
  Integer userId;
  String userName;
  String photo;
  ReservationUserState userState;
  String realName; // 仅用于发送给场地管理方，返回给前端时隐藏信息
  String phone;    // 仅用于发送给场地管理方，返回给前端时隐藏信息
}
