package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.DepartmentDto;
import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    
    @Override
    public Department createDepartment(DepartmentDto departmentDto) {
        Department department = Department.builder()
                .name(departmentDto.getName())
                .note(departmentDto.getNote())
                .user(departmentDto.getUser())
                .vacancyDepartment(departmentDto.getVacancyDepartment())
                .build();
        return departmentRepository.save(department);
    }

    @Override
    public List<DepartmentDto> selectAllDepartment() {
        List<Department> lists = departmentRepository.findAll();
        List<DepartmentDto> departments = new ArrayList<>();
        Iterator<Department> itr = lists.iterator();
        while(itr.hasNext()) {
            Department department = itr.next();
            DepartmentDto dto = DepartmentDto.builder()
                    .name(department.getName())
                    .note(department.getNote())
                    .vacancyDepartment(department.getVacancyDepartment())
                    .user(department.getUser())
                    .build();
        }
        return departments;
    }

    @Override
    public void updateDepartment(DepartmentDto updatedDepartmentDto) {
        Department department = departmentRepository.findById(updatedDepartmentDto.getId()).get();
        department.setName(updatedDepartmentDto.getName());
        department.setNote(updatedDepartmentDto.getNote());
        departmentRepository.save(department);
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
//                .orElseThrow(()-> new UsernameNotFoundException("Department not found"));
    }
}
