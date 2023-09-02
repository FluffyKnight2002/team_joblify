package com.ace_inspiration.team_joblify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PineData {
    private String year;
    private String month;
    private String position;
}
