package com.ace_inspiration.team_joblify.service.hr_service;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;

import com.ace_inspiration.team_joblify.entity.InterviewProcess;

public interface InterviewProcessService {
	
	DataTablesOutput<InterviewProcess> getAll(DataTablesInput input);

}
