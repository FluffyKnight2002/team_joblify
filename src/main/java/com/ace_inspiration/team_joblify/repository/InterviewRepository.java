package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Interview;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends DataTablesRepository<Interview, Long>{
}
