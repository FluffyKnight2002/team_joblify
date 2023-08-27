package com.ace_inspiration.team_joblify.entity;

import java.time.LocalDate;

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
	    private long id;
		
		@Column(name="name")
		private String name;
		
		@Column(name="apply_date")
		private String apply_date;
		
		@Column(name="interview_date")
	    private String interview_date;
		
		@Column(name="interview_status")
		private String interview_status;
		
		@Column(name="dob")
	    private LocalDate dob;
		
	    @Enumerated(EnumType.STRING)
	    @Column(name="gender")
	    private Gender gender;

	    @Column(name="phone")
	    private String phone;
	    
	    @Column(name="email")
	    private String email;
	    
	    @Column(name="education")
	    private String education;
	    
	    @Column(name="apply_position")
	    private String apply_position;
	    
	    @Enumerated(EnumType.STRING)
	    private Level lvl;
	    
	    @Column(name="tech_skills")
	    private String tech_skills;
	    
	    @Column(name="specialist_tech")
	    private String specialist_tech;
	    
	    @Column(name="experience")
	    private String experience;
	    
	    @Column(name="expected_salary")
	    private double expected_salary;
	    
	    @Column(name="language_skills")
	    private String language_skills;	   
}
