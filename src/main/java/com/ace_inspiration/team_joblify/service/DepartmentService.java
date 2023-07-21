package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.entity.Department;

import java.util.List;

public interface DepartmentService {

    Department createDepartment(Department department);
    List<Department> selectAllDepartment();
    void removeDepartment(long id);
    List<Department> findByNameContainingIgnoreCase(String term);
    Department findByName(String departmentName);
    Department checkAndSetDepartment(String departmentName);
    void autoFillDepartment(String newName);
    Department convertDepartment(String departmentName);
}
