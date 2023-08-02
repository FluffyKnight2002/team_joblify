package com.ace_inspiration.team_joblify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private int hiredPost;
    @JsonInclude(JsonInclude.Include.ALWAYS)
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
    private String onSiteOrRemote;
    private String lvl;
    private String salary;
    private String status;
    private long updatedUserId;
    private String updatedUsername;
    private LocalDateTime updatedTime;
    private long createdUserId;
    private String createdUsername;
    private LocalDateTime createdDateTime;
    private LocalDate openDate;
    private LocalDate closeDate;
    private String note;

}
