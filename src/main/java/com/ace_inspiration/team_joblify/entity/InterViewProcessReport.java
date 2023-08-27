package com.ace_inspiration.team_joblify.entity;

import java.time.LocalDate;

import com.querydsl.core.annotations.Immutable;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;




@Entity
@Immutable
@Data
@Table(name="all_post")
public class InterViewProcessReport {
	@Id
	private long id;
	
	private LocalDate close_date;
	
	private LocalDate open_date;
	
	private String position;
	
	private long total_candidates;
	
	private long interview_candidate;
	
	private long passed_candidates;
	
	private long pending_candidates;
	
	private long cancel_candidates;
	
	private long not_interview_candidates;
	
	private long accepted_candidates;

	private long interviewed_counts;
}
