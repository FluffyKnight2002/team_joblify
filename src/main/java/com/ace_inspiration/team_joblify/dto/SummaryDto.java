package com.ace_inspiration.team_joblify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.Gender;
import com.ace_inspiration.team_joblify.entity.LanguageSkills;
import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.entity.Status;
import com.ace_inspiration.team_joblify.entity.TechSkills;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryDto {

	  private long id;
	   
	   private String name;
	   
	   private String email;
	   
	   private Status selectionStatus;
	   
	   private Status interviewStatus;
	   
	   private LocalDate dob;
	   
	   private String apply_position;
	   
	   private String education;
	   
	   private String experience;
	   
	   private double expected_salary;
	   
	   private Gender gender;
	   
	   private Level lvl;
	   
	   private String phone;
	   
	   private String specialist_tech;
	   
	   private String pId;
	
}
