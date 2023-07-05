package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Summary;
import com.ace_inspiration.team_joblify.entity.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryRepsitory extends DataTablesRepository<Long, Summary> {
}
