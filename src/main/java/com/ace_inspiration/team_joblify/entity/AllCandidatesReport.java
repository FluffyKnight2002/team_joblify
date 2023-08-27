package com.ace_inspiration.team_joblify.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.querydsl.core.annotations.Immutable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Immutable
@Data
@Table(name="all_candidates")
public class AllCandidatesReport {
		@Id
		@Column(name="candidate_id")
	    private long candidateId;
		
		@Column(name="name")
		private String name;
		
		@Column(name="apply_date")
		private LocalDateTime applyDate;
		
		@Column(name="interview_status")
		@Enumerated(EnumType.STRING)
		private Status interviewStatus;

		@Column(name="selection_status")
		@Enumerated(EnumType.STRING)
	    private Status selectionStatus;

	    private String type;

		@Column(name="apply_position")
	    private String applyPosition;

		@Column(name="dob")
	    private LocalDate dob;
		
	    private String education;

		@Column(name="email")
	    private String email;
	    
	   	@Column(name="expected_salary")
	    private double expectedSalary;
	    
	    private String experience;
	    
	    @Enumerated(EnumType.STRING)
	    private Gender gender;
	    
	    @Enumerated(EnumType.STRING)
	    private Level lvl;			    

	    private String phone;

		@Column(name="specialist_tech")
	    private String specialistTech;

		@Column(name="language_skills")
	    private String languageSkills;

	    @Column(name="tech_skills")
	    private String techSkills;
	    
 		@Column(name="interview_dates")
	    private String interviewDates;
	    
		@Column(name="interview_times")
	    private String interviewTimes;

		@Column(name="interview_stages")
	    private String interviewStages;

		@Column(name="interview_types")
	    private String interviewTypes;
}
