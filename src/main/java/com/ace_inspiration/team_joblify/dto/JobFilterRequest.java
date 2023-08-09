package com.ace_inspiration.team_joblify.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JobFilterRequest {

    private String sortBy;
    private String datePosted;
    private String position;
    private String jobType;
    private String[] level;
    private boolean isUnder10;
    private boolean isIncludingClosed;
}
