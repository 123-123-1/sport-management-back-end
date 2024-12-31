package com.tongji.sportmanagement.DTO;
import java.util.List;

import com.tongji.sportmanagement.Entity.Court;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourtListDTO
{
    List<Court> courts;
}
