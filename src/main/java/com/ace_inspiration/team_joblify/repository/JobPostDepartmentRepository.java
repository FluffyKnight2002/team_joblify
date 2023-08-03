package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.VacancyInfo;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface JobPostDepartmentRepository extends DataTablesRepository<VacancyInfo, Long> {
}
