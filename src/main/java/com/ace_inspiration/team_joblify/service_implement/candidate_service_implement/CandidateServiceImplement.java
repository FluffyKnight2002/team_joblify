package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import java.util.List;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.repository.CandidateRepository;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateServiceImplement implements CandidateService{
	
	private final CandidateRepository candidateRepository;
	
	@Override
	public DataTablesOutput<Candidate> getAllcandidate(DataTablesInput input){
		return candidateRepository.findAll(input);
	}
}
