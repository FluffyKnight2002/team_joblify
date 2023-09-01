package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.InterviewProcess;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewProcessRepository extends DataTablesRepository<InterviewProcess,Long> {

    DataTablesOutput<InterviewProcess> findAll(DataTablesInput input, Specification<InterviewProcess> specification);
}
