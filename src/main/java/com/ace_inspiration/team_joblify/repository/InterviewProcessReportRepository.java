package com.ace_inspiration.team_joblify.repository;
import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.ace_inspiration.team_joblify.entity.AllPost;
import com.ace_inspiration.team_joblify.entity.InterViewProcessReport;

@Repository
public interface InterviewProcessReportRepository extends DataTablesRepository<InterViewProcessReport,Long> {

	
}
