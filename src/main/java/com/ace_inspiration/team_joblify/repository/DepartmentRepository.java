package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Department;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends DataTablesRepository<Department, Long> {

    List<Department> findByNameContainingIgnoreCase(String term);
    Optional<Department> findByName(String name);
}
