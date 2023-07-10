package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.JobPostDepartment;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface JobPostDepartmentRepository extends DataTablesRepository<JobPostDepartment, Long> {
}
