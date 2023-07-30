package com.ace_inspiration.team_joblify.entity;

import com.querydsl.core.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Immutable
@Table(name="interview_process")
@Data
public class InterviewProcess {
	@Id
	private long id;
	 
	private String name;
	
	private String email;
	
	@Column(name = "selection_status")
	private Status selectionStatus;
	
	@Column(name = "interview_status")
	private Status interviewStatus;
	
	private String position;
}
