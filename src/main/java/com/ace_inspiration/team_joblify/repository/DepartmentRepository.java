package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Department;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface DepartmentRepository extends DataTablesRepository<Department, Long> {
}
