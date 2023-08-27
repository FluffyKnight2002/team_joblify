package com.ace_inspiration.team_joblify.entity;

import com.querydsl.core.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Immutable
@Table(name="all_post")
public class AllPost {
	
	@Id
	private long id;
	
	@Column(name="close_date")
	private String closeDate;

	@Column(name="open_date")
	private String openDate;
	
	private String position;
	
	@Column(name="total_candidates")
	private Long totalCandidate;
	
	@Column(name="passed_candidates")
	private Long passedCandidate;
	
	@Column(name="pending_candidates")
	private Long pendingCandidate;
	
	@Column(name="cancel_candidates")
	private Long cancelCandidate;
	
	@Column(name="not_interview_candidates")
	private Long notInterviewCandidate;
	
	@Column(name="accepted_candidates")
	private Long acceptedCandidate;
	
	@Column(name="interviewed_counts")
	private Long interviewedCounts;

}
