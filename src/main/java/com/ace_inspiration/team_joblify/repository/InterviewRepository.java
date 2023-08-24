package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Interview;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends DataTablesRepository<Interview, Long>{

	 List<Interview> findInterviewsByCandidateId(long candidateId);
	 List<String> findInterviewStageByCandidateId(long candidateId);
}
