package com.ace_inspiration.team_joblify.service.candidate_service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.entity.Candidate;

@Service
public interface CandidateService  {
	// Create
	public void saveCandidate(CandidateDto candidateDto); 
	List<Candidate> getAllCandidates(); 
}