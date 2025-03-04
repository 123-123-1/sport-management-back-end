package com.tongji.sportmanagement.VenueSubsystem.DTO;
import java.util.List;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueListDTO
{
    long total;
    int page;
    List<VenueDetailDTO> venues;
}
