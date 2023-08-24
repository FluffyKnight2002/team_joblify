package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Candidate;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends DataTablesRepository<Candidate, Long> {
	
	List<Candidate> findByIdIn(List<Long> ids);
}
