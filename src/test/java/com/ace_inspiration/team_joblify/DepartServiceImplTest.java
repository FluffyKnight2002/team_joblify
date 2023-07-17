package com.ace_inspiration.team_joblify;

import com.ace_inspiration.team_joblify.dto.DepartmentDto;
import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.service_implement.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DepartServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDepartmentTest() {
        Department department = TestUtil.createMockDepartment();
        DepartmentDto departmentDto = TestUtil.createMockDepartmentDto();

        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        departmentService.createDepartment(departmentDto);

        verify(departmentRepository,times(1)).save(any(Department.class));
    }

    @Test
    void selectAllDepartmentsTest() {
        List<Department> departments = TestUtil.createMockDepartmentList();

        when(departmentRepository.findAll()).thenReturn(departments);

        List<DepartmentDto> result = departmentService.selectAllDepartment();

        verify(departmentRepository,times(1)).findAll();
    }

    @Test
    void updateDepartmentTest() {
        DepartmentDto departmentDto = TestUtil.createMockDepartmentDto();
        departmentDto.setId(1L);
        Department department = TestUtil.createMockDepartment();

        when(departmentRepository.findById(departmentDto.getId())).thenReturn(Optional.of(department));

        departmentService.updateDepartment(departmentDto);

        verify(departmentRepository,times(1)).findById(departmentDto.getId());
        verify(departmentRepository,times(1)).save(any(Department.class));

        assertEquals(departmentDto.getName(),department.getName());
    }

    @Test
    void deleteDepartment() {

        long id = 1L;

        departmentService.removeDepartment(id);

        verify(departmentRepository,times(1)).deleteById(id);
    }

}
