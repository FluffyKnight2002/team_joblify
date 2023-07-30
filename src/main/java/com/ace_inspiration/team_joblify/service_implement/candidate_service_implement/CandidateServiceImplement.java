package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.entity.Status;
import com.ace_inspiration.team_joblify.repository.CandidateRepository;
import com.ace_inspiration.team_joblify.repository.PositionRepository;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateServiceImplement implements CandidateService{
	
	private final CandidateRepository candidateRepository;
	
	
	@Autowired
    private EntityManager entityManager;
	
	@Override
	public DataTablesOutput<Candidate> getAllcandidate(DataTablesInput input){
		return candidateRepository.findAll(input);
	}
	
	
	@Override
	public Optional<Candidate> findByid(long id) {
		return candidateRepository.findById(id);
		  
	}
	
	@Override
	@Transactional
    public void changeStatus(long id) {
      Candidate candidate = entityManager.find(Candidate.class, id);
	        if (candidate != null) {
	            candidate.setSelectionStatus(Status.VIEWED); // Set the new status value
	            entityManager.persist(candidate); // Save the updated candidate entity
	        }
	    }
}
