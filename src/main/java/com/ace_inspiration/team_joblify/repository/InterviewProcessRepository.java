package com.ace_inspiration.team_joblify.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.ace_inspiration.team_joblify.entity.InterviewProcess;

@Repository
public interface InterviewProcessRepository extends DataTablesRepository<InterviewProcess,Long> {

}
