package com.ace_inspiration.team_joblify.dto;

import com.ace_inspiration.team_joblify.entity.JobType;
import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.entity.OnSiteOrRemote;
import com.ace_inspiration.team_joblify.entity.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VacancyFilteredDto {

    private long id;

    private LocalDate closeDate;

    private String description;

    private int hiredPost;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private Level level;

    private String note;

    @Enumerated(EnumType.STRING)
    private OnSiteOrRemote onSiteOrRemote;

    private LocalDate openDate;

    private int post;

    private String preferences;

    private String requirements;

    private String responsibilities;

    private String salary;

    private LocalDateTime updatedTime;

    private String workingHours;

    private String workingDays;

    private long applicants;

    private long vacancyId;

    private LocalDateTime createdTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String position;

    private String department;

    private String address;

    private String createdUsername;

    private String updatedUsername;

    private int totalResults;
}
