package com.ace_inspiration.team_joblify.entity;

import java.time.LocalDate;

import com.querydsl.core.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Immutable
@Table(name = "all_post")
public class AllPost {

	@Id
	private long id;

	@Column(name = "close_date")
	private LocalDate closeDate;

	@Column(name = "open_date")
	private LocalDate openDate;

	private String position;

	@Column(name = "total_candidates")
	private long totalCandidate;

	@Column(name = "passed_candidates")
	private long passedCandidate;

	@Column(name = "pending_candidates")
	private long pendingCandidate;

	@Column(name = "cancel_candidates")
	private long cancelCandidate;

	@Column(name = "not_interview_candidates")
	private long notInterviewCandidate;

	@Column(name = "accepted_candidates")
	private long acceptedCandidate;

	@Column(name = "interviewed_counts")
	private long interviewedCounts;

	@Column(name = "offered_letter_email")
	private long offeredLetterEmail;
}
