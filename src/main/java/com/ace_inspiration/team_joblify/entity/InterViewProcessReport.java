package com.ace_inspiration.team_joblify.entity;

import java.time.LocalDate;

import com.querydsl.core.annotations.Immutable;

import jakarta.persistence.Column;
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
	
	@Column(name = "close_date")
	private LocalDate closeDate;
	
	@Column(name = "open_date")
	private LocalDate openDate;
	
	@Column(name="position")
	private String position;
	
	@Column(name="total_candidates")
	private long totalCandidates;
	
	@Column(name="interviews_candidate")
	private long interviewCandidates;
	
	@Column(name="passed_candidates")
	private long passCandidates;
	
	@Column(name="pending_candidates")
	private long pendingCandidates;
	
	@Column(name="cancel_candidates")
	private long cancelCandidates;
	
	@Column(name="not_interview_candidates")
	private long notInterviewCandidates;
	
	@Column(name="accepted_candidates")
	private long acceptedCandidates;

	@Column(name="interviewed_count")
	private long interviewCount;
}
