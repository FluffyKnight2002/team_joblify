package com.ace_inspiration.team_joblify.entity;


import com.querydsl.core.annotations.Immutable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Immutable
@Table(name="interview_process")
public class InterviewProcess  {
	@Id
	private long id;
	
	@Column(name="vi_id")
	private long viId;
	
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
	
	private LocalDateTime date;
}
