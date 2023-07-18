package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Department;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.Optional;

public interface DepartmentRepository extends DataTablesRepository<Department, Long> {

    @Query(value = "SELECT * FROM department WHERE name LIKE %:term%", nativeQuery = true)
    List<Department> findByNameContainingIgnoreCase(String term);
    @Query(value = "SELECT * FROM department WHERE name = :departmentName", nativeQuery = true)
    Department findByName(String departmentName);
    Optional<Department> findByName(String name);
}
