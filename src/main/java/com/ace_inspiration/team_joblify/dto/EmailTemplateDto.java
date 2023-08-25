package com.ace_inspiration.team_joblify.dto;


import com.ace_inspiration.team_joblify.entity.Candidate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateDto {
   private String to;
   private String[] ccmail;
   private String subject;
   private String name;
   private String content;
   private String time;
   private String date;
   private String status;
   private String type;
   private long canId;

}
