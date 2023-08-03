package com.ace_inspiration.team_joblify.entity;

<<<<<<< HEAD
=======


>>>>>>> backup
import com.querydsl.core.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
<<<<<<< HEAD
=======
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
>>>>>>> backup
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Immutable
@Table(name="interview_process")
@Data
<<<<<<< HEAD
public class InterviewProcess {
	@Id
	private long id;
	 
=======
public class InterviewProcess  {
	@Id
	private long id;
	
>>>>>>> backup
	private String name;
	
	private String email;
	
	@Column(name = "selection_status")
<<<<<<< HEAD
	private Status selectionStatus;
	
	@Column(name = "interview_status")
=======
	@Enumerated(EnumType.STRING)
	private Status selectionStatus;
	
	private String phone;
	
	@Enumerated(EnumType.STRING)
	private Level lvl;
	
	private String experience;
	
	@Column(name = "interview_status")
	@Enumerated(EnumType.STRING)
>>>>>>> backup
	private Status interviewStatus;
	
	private String position;
}
