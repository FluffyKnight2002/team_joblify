package com.ace_inspiration.team_joblify.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountDto {

	
	private long id;
	
	
	private int  postTotal;
	
	private int hired;
	
	private long totalCandidates;
	
	private LocalDate close;
	
	private LocalDate open;
	
	
	
}
