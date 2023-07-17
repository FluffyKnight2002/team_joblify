package com.ace_inspiration.team_joblify.dto;

import com.ace_inspiration.team_joblify.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacancyDto {

    private long id;
    private String position;
    private int post;
    private String type;
    private String department;
    private String descriptions;
    private String requirements;
    private String preferences;
    private String responsibilities;
    private String address;
    private String workingDays;
    private String workingHours;
    private String lvl;
    private String salary;
    private Status status;

}
