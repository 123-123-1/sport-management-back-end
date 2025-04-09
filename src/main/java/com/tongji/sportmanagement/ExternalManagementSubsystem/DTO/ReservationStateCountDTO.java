package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationStateCountDTO
{
  ReservationState state;
  Long count;  
}
