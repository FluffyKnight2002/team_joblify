package com.ace_inspiration.team_joblify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


import com.ace_inspiration.team_joblify.entity.Gender;
import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.entity.Status;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDto {

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
