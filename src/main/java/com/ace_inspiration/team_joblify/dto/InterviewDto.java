package com.ace_inspiration.team_joblify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewDto {

    private String interviewStage;

    private String interviewType;

    private String interviewTime;

    private String interviewDate;

    private String note;
}
