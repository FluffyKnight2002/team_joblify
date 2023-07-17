package com.ace_inspiration.team_joblify;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.entity.Vacancy;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.PositionRepository;
import com.ace_inspiration.team_joblify.repository.VacancyRepository;
import com.ace_inspiration.team_joblify.service.DepartmentService;
import com.ace_inspiration.team_joblify.service.PositionService;
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
    private VacancyRepository vacancyRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private DepartmentService departmentService;
    @Mock
    private PositionRepository positionRepository;
    @Mock
    private PositionService positionService;

    @InjectMocks
    private VacancyServiceImpl vacancyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createVacancyTest() {
        VacancyDto vacancyDto = TestUtil.createMockVacancyDto();
        Vacancy vacancy = TestUtil.createMockVacancy();

        when(vacancyRepository.save(any(Vacancy.class))).thenReturn(vacancy);

        vacancyService.createVacancy(vacancyDto);

        verify(vacancyRepository, times(1)).save(any(Vacancy.class));
    }

    @Test
    void createPositionRepository() {
        assertNotNull(positionRepository);
    }

    @Test
    void checkAndSetPosition() {
        String positionName = "New Position";

        if (positionRepository.findByName(positionName) == null) {
            autoFillPosition(positionName);
        }
        Position position = convertPosition(positionName);

        assertNotNull(position);

        assertNotNull(position.getName());
    }
    void autoFillPosition(String newName) {
        Position newPosition = Position.builder()
                .name(newName)
                .build();
        positionService.addPosition(newPosition);
    }
    Position convertPosition(String positionName) {
        Position position = positionRepository.findByName(positionName);
        return position;
    }

    @Test
    void selectAllVacancyTest() {
        List<Vacancy> vacancies = TestUtil.createMockVacancyList();

        when(vacancyRepository.findAll()).thenReturn(vacancies);

        List<VacancyDto> result = vacancyService.selectAllVacancy();

        verify(vacancyRepository, times(1)).findAll();
        // Assert the result is as expected
        assertTrue(result != null);
    }

    @Test
    void updateVacancyTest() {
        VacancyDto vacancyDto = TestUtil.createMockVacancyDto();
        vacancyDto.setId(1L);
        Vacancy vacancy = TestUtil.createMockVacancy();

        when(vacancyRepository.findById(vacancyDto.getId())).thenReturn(Optional.of(vacancy));
        when(vacancyRepository.save(any(Vacancy.class))).thenReturn(vacancy);

        vacancyService.updateVacancy(vacancyDto);

        verify(vacancyRepository, times(1)).findById(vacancyDto.getId());
        verify(vacancyRepository, times(1)).save(any(Vacancy.class));

        // Assert the vacancy properties are updated correctly
        // Example assertion:
        assertEquals(vacancyDto.getPosition(), vacancy.getPosition());
    }


    @Test
    void deleteVacancyTest() {
        long id = 1L;

        vacancyService.deleteVacancy(id);

        verify(vacancyRepository, times(1)).deleteById(id);
    }
}
