package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    
    @Override
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> selectAllDepartment() {
        return departmentRepository.findAll();
    }

    @Override
    public void removeDepartment(long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public List<Department> findByNameContainingIgnoreCase(String term) {
        return departmentRepository.findByNameContainingIgnoreCase(term);
    }

    @Override
    public Department findByName(String departmentName) {
        return departmentRepository.findByName(departmentName).orElse(null);
    }

    @Override
    public Department checkAndSetDepartment(String departmentName) {

        if(departmentRepository.findByName(departmentName).isEmpty()) {
            autoFillDepartment(departmentName);
        }

        return convertDepartment(departmentName);
    }

    @Override
    public void autoFillDepartment(String newName) {
        Department department = Department.builder()
                .name(newName)
                .build();
        departmentRepository.save(department);
    }

    @Override
    public Department convertDepartment(String departmentName) {
        return departmentRepository.findByName(departmentName).orElse(null);
    }
}
