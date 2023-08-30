package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.AllCandidatesReport;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.ace_inspiration.team_joblify.entity.InterviewProcess;

@Repository
public interface InterviewProcessRepository extends DataTablesRepository<InterviewProcess,Long> {

}
