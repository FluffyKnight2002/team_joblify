package com.ace_inspiration.team_joblify.service;

import java.util.List;

import com.ace_inspiration.team_joblify.dto.EmailTemplateDto;
import com.ace_inspiration.team_joblify.entity.Interview;

public interface InterviewService {
	void saveInterview(EmailTemplateDto emailTemplateDto);
	   List<Interview> findInterviewsByCandidateId(long candidateId);
	   List<String> findInterviewStageByCandidateId(long candidateId);
}
