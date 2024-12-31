package com.tongji.sportmanagement.DTO;
import java.util.List;

import com.tongji.sportmanagement.Entity.Venue;

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
    List<Venue> venues;
}
