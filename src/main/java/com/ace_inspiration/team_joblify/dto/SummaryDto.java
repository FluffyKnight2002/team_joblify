package com.ace_inspiration.team_joblify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryDto {


    private String name;


    private Date dob;


    private String gender;


    private String phone;


    private String email;


    private String education;


    private String[] languageSkills;


    private String[] techSkills;


    private String applyPosition;


    private String lvl;


    private String specialistTech;


    private String experience;


    private int expectedSalary;
}
