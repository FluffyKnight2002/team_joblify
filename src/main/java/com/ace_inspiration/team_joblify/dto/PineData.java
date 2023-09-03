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
    private int year;
    private int month;
    private String position;
    private int total_candidates;
    private int  passed_candidates;
    private int pending_candidates;
    private int cancel_candidates;
    private int not_interview_candidates;
    private int accepted_candidates;
    private int interviewed_counts;
    private int offered_letter_mail;
}
