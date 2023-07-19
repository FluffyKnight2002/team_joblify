package com.ace_inspiration.team_joblify;

import com.ace_inspiration.team_joblify.dto.DepartmentDto;
import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.Address;
import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.entity.Vacancy;
import com.ace_inspiration.team_joblify.entity.VacancyDepartment;
import com.ace_inspiration.team_joblify.repository.AddressRepository;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.PositionRepository;
import com.ace_inspiration.team_joblify.repository.VacancyDepartmentRepository;
import com.ace_inspiration.team_joblify.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VacancyDepartmentServiceImplTest {

    @Mock
    private VacancyDepartmentRepository vacancyDepartmentRepository;

    @Mock
    private VacancyService vacancyService;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private PositionService positionService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private VacancyDepartmentService vacancyDepartmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatedVacancyDepartments() {
        // Create a mock VacancyDto
        VacancyDto vacancyDto = MockVacancyDto.createMockVacancyDto();

        // Create a mock Address
        Address address = Address.builder()
                .id(1l)
                .name("Mock Address")
                .build();
        when(addressService.findByName(any(String.class))).thenReturn(null);
        when(addressService.addAddress(any(Address.class))).thenReturn(address);

        // Create a mock Department
        Department department = new Department();
        when(departmentService.findByName(any(String.class))).thenReturn(null);
        when(departmentService.createDepartment(any(DepartmentDto.class))).thenReturn(department);

        // Create a mock Vacancy
        Vacancy vacancy = new Vacancy();
        when(vacancyService.createVacancy(any(VacancyDto.class))).thenReturn(vacancy);

        // Mock the vacancyDepartmeAntRepository.save() method

        // Call the method under test
        vacancyDepartmentService.createdVacancyDepartments(vacancyDto);

        // Verify the interactions
        verify(addressService).findByName(any(String.class));
        verify(addressService).addAddress(any(Address.class));
        verify(departmentService).findByName(any(String.class));
        verify(departmentService).createDepartment(any(DepartmentDto.class));
        verify(vacancyService).createVacancy(any(VacancyDto.class));
        verify(vacancyDepartmentRepository).save(any(VacancyDepartment.class));
    }

    @Test
    void testSelectAllVacancyDepartments() {
        // TODO: Implement this test
    }

    @Test
    void testUpdateVacancyDepartments() {
        // TODO: Implement this test
    }

    @Test
    void testClosedVacancyDepartments() {
        // TODO: Implement this test
    }
}
