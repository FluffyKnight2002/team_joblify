package com.ace_inspiration.team_joblify.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PindChartDto {
	private long total;
	  private long not;
	  private long interviewed;
	    private long panding;
	    private long passed;
	    private long cancel;
}
