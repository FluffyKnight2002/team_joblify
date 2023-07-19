package com.ace_inspiration.team_joblify.dto;

import com.ace_inspiration.team_joblify.entity.Status;
import com.ace_inspiration.team_joblify.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacancyDto {

    private long id;
    private String position;
    private int post;
    private int requiredPost;
    private int applicants;
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
    private DepartmentDto departmentDto;
    private User updatedUser;
    private User creadedUser;
    private LocalDateTime createdDateTime;
    private LocalDate openDate;
    private LocalDate closeDate;
    private String note;

}
