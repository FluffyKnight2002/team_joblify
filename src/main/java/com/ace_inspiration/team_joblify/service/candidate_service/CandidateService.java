package com.ace_inspiration.team_joblify.service.candidate_service;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.entity.Candidate;

public interface CandidateService {

	DataTablesOutput<Candidate> getAllcandidate(DataTablesInput input);
}
