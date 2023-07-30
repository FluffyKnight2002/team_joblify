package com.ace_inspiration.team_joblify.service.candidate_service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.Position;

public interface CandidateService {

	DataTablesOutput<Candidate> getAllcandidate(DataTablesInput input);
	 void changeStatus(long id);
	 Optional<Candidate> findByid(long id);
	
}
