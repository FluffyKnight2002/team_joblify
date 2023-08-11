package com.ace_inspiration.team_joblify.service.candidate_service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;import com.ace_inspiration.team_joblify.dto.CandidateDto;

import com.ace_inspiration.team_joblify.entity.Candidate;


@Service
public interface CandidateService  {
	// Create
	public void saveCandidate(CandidateDto candidateDto);
	List<Candidate> getAllCandidates();
    DataTablesOutput<Candidate> getAllcandidate(DataTablesInput input);
    void changeStatus(long id);
    Optional<Candidate> findByid(long id);
    void changeInterviewstatus(long id,String status);

}
