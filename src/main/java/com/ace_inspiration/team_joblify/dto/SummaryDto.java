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

	private LocalDate dob;

	private String gender;

	private String phone;

	private String email;

	private String education;

	private String applyPosition;

	private String lvl;

	private String specialistTech;

	private String experience;

	private double expectedSalary;

	private Candidate candidate;

	private List<LanguageSkills> languageSkills = new ArrayList<>();

	private List<TechSkills> techSkills = new ArrayList<>();
	
}
