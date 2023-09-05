package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.ace_inspiration.team_joblify.repository.CandidateRepository;
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
	private final CandidateRepository candidateRepository;
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

	@Override
	public void savefirst(long id){
		Optional<Candidate> candiDate=candidateRepository.findById(id);
		LocalDateTime dateTime = LocalDateTime.now();
		System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		// Create a DateTimeFormatter for formatting just the time
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		// Format the LocalDateTime as a string with only the time
		String timeString = dateTime.format(timeFormatter);  DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// Format the LocalDateTime as a string with only the date
		String dateString = dateTime.format(dateFormatter);


		if (candiDate.isPresent()) {
			Candidate candidate = candiDate.get();
			Optional<Interview> interview = interviewRepository.findByIdAndInterviewStage(id, InterviewStage.FIRST);
			if (interview.isEmpty()) {
				System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
				Interview interview1 = new Interview();
				interview1.setInterviewStage(InterviewStage.FIRST);
				interview1.setCandidate(candiDate.get());
				interview1.setInterviewTime(timeString);
				interview1.setInterviewDate(dateString);
				interview1.setType(InterviewType.ONLINE);
				interview1.setCandidate(candidate);
				interviewRepository.save(interview1);
			}
		}
	}
	
	
	
	
}
