package com.ace_inspiration.team_joblify;

import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.service_implement.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

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
        Department departmentDto = TestUtil.createMockDepartmentDto();

        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        departmentService.createDepartment(department);

        verify(departmentRepository,times(1)).save(any(Department.class));
    }

    @Test
    void selectAllDepartmentsTest() {
        List<Department> departments = TestUtil.createMockDepartmentList();

        when(departmentRepository.findAll()).thenReturn(departments);

        List<Department> result = departmentService.selectAllDepartment();

        verify(departmentRepository,times(1)).findAll();
    }

    @Test
    void deleteDepartment() {

        long id = 1L;

        departmentService.removeDepartment(id);

        verify(departmentRepository,times(1)).deleteById(id);
    }

}
