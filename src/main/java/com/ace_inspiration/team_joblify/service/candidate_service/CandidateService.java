package com.ace_inspiration.team_joblify.service.candidate_service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.dto.SummaryDto;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.Position;

public interface CandidateService {

	DataTablesOutput<Candidate> getAllcandidate(DataTablesInput input);
	 void changeStatus(long id);
	 SummaryDto findByid(long id);
	void changeInterviewstatus(long id,String status);
    Candidate saveCandidate(CandidateDto candidateDto);
    List<Candidate> getAllCandidates();
    void stage(long id);
    List<Candidate> getFile(List<Long> id);

}