package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Department;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import java.util.Optional;

public interface DepartmentRepository extends DataTablesRepository<Department, Long> {
    Optional<Department> findByName(String name);
}
