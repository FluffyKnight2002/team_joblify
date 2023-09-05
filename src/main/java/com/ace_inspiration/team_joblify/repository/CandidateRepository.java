package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Candidate;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends DataTablesRepository<Candidate, Long> {
	
	List<Candidate> findByIdIn(List<Long> ids);
	int countBy();
}
