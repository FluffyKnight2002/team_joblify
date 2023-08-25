package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ace_inspiration.team_joblify.dto.EmailTemplateDto;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.Interview;
import com.ace_inspiration.team_joblify.entity.InterviewStage;
import com.ace_inspiration.team_joblify.entity.InterviewType;
import com.ace_inspiration.team_joblify.repository.InterviewProcessRepository;
import com.ace_inspiration.team_joblify.repository.InterviewRepository;
import com.ace_inspiration.team_joblify.service.InterviewService;
import com.ace_inspiration.team_joblify.service.hr_service.InterviewProcessService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewServiceImplement implements InterviewService {

	private final InterviewRepository interviewRepository;

	@Override
	public void saveInterview(EmailTemplateDto emailTemplateDto) {
	   	Candidate candidate = new Candidate();
    	candidate.setId(emailTemplateDto.getCanId());
    	
    	Interview interview=new Interview();
    	interview.setInterviewDate(emailTemplateDto.getDate());
    	interview.setInterviewTime(emailTemplateDto.getTime());
    	interview.setType(InterviewType.valueOf(emailTemplateDto.getType()));
    	interview.setInterviewStage(InterviewStage.valueOf(emailTemplateDto.getStatus()));
    	interview.setCandidate(candidate);
    	interviewRepository.save(interview);
		
	}

	@Override
	public List<Interview> findInterviewsByCandidateId(long candidateId) {
		
		return interviewRepository.findInterviewsByCandidateId(candidateId);
	}

	@Override
	public List<String> findInterviewStageByCandidateId(long candidateId) {
		
		return interviewRepository.findInterviewStageByCandidateId(candidateId);
	}
	
	
	
	
}
