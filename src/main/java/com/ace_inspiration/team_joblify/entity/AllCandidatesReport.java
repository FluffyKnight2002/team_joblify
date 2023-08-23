package com.ace_inspiration.team_joblify.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.annotations.Immutable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Immutable
@Data
@Table(name="all_candidates")
public class AllCandidatesReport {
		@Id
	    private long id;
		private String name;
		@Column(name="apply_date")
		private String applyDate;
		@Column(name="interview_date")
	    private String interViewDate;
		@Column(name="interview_status")
		private String interviewStatus;
	    private LocalDate dob;
	    @Enumerated(EnumType.STRING)
	    private Gender gender;
	    private String phone;
	    private String email;
	    private String education;
	    private String applyPosition;
	    @Enumerated(EnumType.STRING)
	    private Level lvl;
	    private String techSkills;
	    private String specialistTech;
	    private String experience;
	    private double expectedSalary;
	    private String languageSkills;	   
}
