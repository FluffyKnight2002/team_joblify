package com.ace_inspiration.team_joblify;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.*;
import com.ace_inspiration.team_joblify.service.AddressService;
import com.ace_inspiration.team_joblify.service.DepartmentService;
import com.ace_inspiration.team_joblify.service.PositionService;
import com.ace_inspiration.team_joblify.service_implement.VacancyInfoServiceImpl;
import com.ace_inspiration.team_joblify.service_implement.VacancyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VacancyServiceImplTest {


    @Mock
    private VacancyInfoRepository vacancyInfoRepository;

    @Mock
    private VacancyRepository vacancyRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private PositionService positionService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private VacancyServiceImpl vacancyService;

    @InjectMocks
    private VacancyInfoServiceImpl vacancyInfoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createVacancyTest() {
        VacancyDto vacancyDto = TestUtil.createMockVacancyDto();
        VacancyInfo vacancyInfo = TestUtil.createMockVacancyInfo();

        when(vacancyInfoRepository.save(any(VacancyInfo.class))).thenReturn(vacancyInfo);

        vacancyInfoService.createdVacancyInfo(vacancyDto);

        // Verify that the vacancyRepository's save method was called once
        verify(vacancyRepository, times(1)).save(any(Vacancy.class));
    }

    @Test
    void createPositionRepository() {
        assertNotNull(positionRepository);
    }

    @Test
    void selectAllVacancyTest() {
        List<VacancyInfo> vacancies = TestUtil.createMockVacancyList();

        when(vacancyInfoRepository.findAll()).thenReturn(vacancies);

        List<VacancyDto> result = vacancyService.selectAllVacancy();

        verify(vacancyRepository, times(1)).findAll();
        // Assert the result is as expected
        assertTrue(result != null);
    }

//    @Test
//    void updateVacancyTest() {
//        VacancyDto vacancyDto = TestUtil.createMockVacancyDto();
//        vacancyDto.setId(1L);
//        Vacancy vacancy = TestUtil.createMockVacancy();
//
//        when(vacancyRepository.findById(vacancyDto.getId())).thenReturn(Optional.of(vacancy));
//        when(vacancyRepository.save(any(Vacancy.class))).thenReturn(vacancy);
//
//        vacancyService.updateVacancy(vacancyDto);
//
//        verify(vacancyRepository, times(1)).findById(vacancyDto.getId());
//        verify(vacancyRepository, times(1)).save(any(Vacancy.class));
//
//        // Assert the vacancy properties are updated correctly
//        // Example assertion:
//        assertEquals(vacancyDto.getPosition(), vacancy.getPosition());
//    }

    @Test
    public void testUpdateVacancy() {

        // Create mock instances for the related entities (Position, Department, Address)
        Position position = mock(Position.class);
        position.setName("Back-end Developer");
        Department department = mock(Department.class);
        department.setName("Software Development");
        Address address = mock(Address.class);
        address.setName("123 Main Street");

        VacancyDto updatedVacancyDto = TestUtil.createMockVacancyDto();

        updatedVacancyDto.setId(1L);
        updatedVacancyDto.setPosition("Back-end Developer");
        updatedVacancyDto.setDepartment("Software Development");
        updatedVacancyDto.setAddress("123 Main Street");
        updatedVacancyDto.setStatus("OPEN");

        // Define the behavior of the mock repositories and entities
        when(vacancyRepository.findById(updatedVacancyDto.getId())).thenReturn(Optional.of(new Vacancy()));
        when(vacancyRepository.save(any(Vacancy.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(positionService.checkAndSetPosition(updatedVacancyDto.getPosition())).thenReturn(position);
        when(departmentService.checkAndSetDepartment(updatedVacancyDto.getDepartment())).thenReturn(department);
        when(addressService.checkAndSetAddress(updatedVacancyDto.getAddress())).thenReturn(address);

        // Call the method to be tested
        VacancyInfo updatedVacancy = vacancyInfoService.updateVacancyInfo(updatedVacancyDto);

        // Perform assertions
        assertNotNull(updatedVacancy);
        assertEquals(updatedVacancyDto.getId(), updatedVacancy.getId());
        assertEquals(position.getName(), updatedVacancy.getVacancy().getPosition());
        assertEquals(department.getName(), updatedVacancy.getVacancy().getDepartment());
        assertEquals(address.getName(), updatedVacancy.getAddress());
        assertEquals(updatedVacancyDto.getStatus(), updatedVacancy.getStatus());

        // Verify that the methods were called with the correct arguments
        verify(vacancyRepository, times(1)).findById(updatedVacancyDto.getId());
        verify(vacancyRepository, times(1)).save(any(Vacancy.class));
        verify(positionService, times(1)).checkAndSetPosition(updatedVacancyDto.getPosition());
        verify(departmentService, times(1)).checkAndSetDepartment(updatedVacancyDto.getDepartment());
        verify(addressService, times(1)).checkAndSetAddress(updatedVacancyDto.getAddress());
    }


    @Test
    void deleteVacancyTest() {
        long id = 1L;

        vacancyService.deleteVacancy(id);

        verify(vacancyRepository, times(1)).deleteById(id);
    }
}
