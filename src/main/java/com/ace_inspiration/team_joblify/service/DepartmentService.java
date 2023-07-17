package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.DepartmentDto;
import com.ace_inspiration.team_joblify.entity.Department;

import java.util.List;

public interface DepartmentService {

    void createDepartment(DepartmentDto departmentDto);
    List<DepartmentDto> selectAllDepartment();
    void updateDepartment(DepartmentDto departmentDto);
    void removeDepartment(long id);
    List<Department> findByNameContainingIgnoreCase(String term);
    Department findbyName(String departmentName);
}
