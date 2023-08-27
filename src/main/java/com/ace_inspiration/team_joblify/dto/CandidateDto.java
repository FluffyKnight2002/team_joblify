package com.ace_inspiration.team_joblify.dto;

import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.Interview;
import com.ace_inspiration.team_joblify.entity.Notification;
import com.ace_inspiration.team_joblify.entity.Summary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDto {

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

	  private String [] languageSkills;

	  private String languageSkillsString;

	  private String [] techSkills;

	  private String techSkillsString;

	  private String selectionStatus;

	  private String interviewStatus;

	  private LocalDateTime applyDate;

	  private String type;

	  private MultipartFile resume;

	  private String note;

	  private Summary summary;

	  private List<Interview>interviews=new ArrayList<>();

	  private List<Notification>notification=new ArrayList<>();

	  private VacancyDto vacancyDto;

}
