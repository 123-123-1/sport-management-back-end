package com.tongji.sportmanagement.ExternalManagementSubsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationStateCountResponseDTO
{
  Long pendingCount;
  Long normalCount;  
}
