package com.ace_inspiration.team_joblify.entity;


import com.querydsl.core.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Immutable
@Table(name="interview_process")
public class InterviewProcess  {
	@Id
	private long id;
	
	private String name;
	
	private String email;
	
	@Column(name = "selection_status")
	@Enumerated(EnumType.STRING)
	private Status selectionStatus;
	
	private String phone;
	
	@Enumerated(EnumType.STRING)
	private Level lvl;
	
	private String experience;
	
	@Column(name = "interview_status")
	@Enumerated(EnumType.STRING)
	private Status interviewStatus;
	
	private String position;
}
