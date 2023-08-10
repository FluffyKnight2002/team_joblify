package com.ace_inspiration.team_joblify.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.ace_inspiration.team_joblify.entity.AllCandidatesReport;


@Repository
public interface AllCandidatesReportRepository extends DataTablesRepository<AllCandidatesReport, Long> {

}
