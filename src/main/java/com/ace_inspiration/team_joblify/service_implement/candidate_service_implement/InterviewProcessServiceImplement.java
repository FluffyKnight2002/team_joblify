package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ace_inspiration.team_joblify.entity.InterviewProcess;
import com.ace_inspiration.team_joblify.repository.InterviewProcessRepository;
import com.ace_inspiration.team_joblify.service.hr_service.InterviewProcessService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewProcessServiceImplement implements InterviewProcessService {
	
	private final InterviewProcessRepository repo;

	  @Override
	    public DataTablesOutput<InterviewProcess> getAll(DataTablesInput input) {
	        DataTablesOutput<InterviewProcess> interviewData = repo.findAll(input);
	        return interviewData;
	    }
	 
}
