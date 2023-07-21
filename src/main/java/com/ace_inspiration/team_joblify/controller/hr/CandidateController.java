package com.ace_inspiration.team_joblify.controller.hr;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.service_implement.candidate_service_implement.CandidateServiceImplement;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CandidateController {
	
	private final CandidateServiceImplement service;
	
	@GetMapping("/allCandidiate")
	public DataTablesOutput<Candidate> getAllCandidate(@Valid DataTablesInput input){
		return service.getAllcandidate(input);
	}
}
