package com.ace_inspiration.team_joblify.dto;

import java.time.LocalDate;
import java.time.Year;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountDto {

	private String month;

	private String postTotal;

	private String hiredPost;

	private String totalApplied;

	
	
}
