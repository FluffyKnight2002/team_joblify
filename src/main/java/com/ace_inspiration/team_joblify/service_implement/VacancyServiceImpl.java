package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.DepartmentDto;
import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.DepartmentRepository;
import com.ace_inspiration.team_joblify.repository.PositionRepository;
import com.ace_inspiration.team_joblify.repository.VacancyRepository;
import com.ace_inspiration.team_joblify.service.DepartmentService;
import com.ace_inspiration.team_joblify.service.PositionService;
import com.ace_inspiration.team_joblify.service.VacancyService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;
    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final PositionService positionService;

    public VacancyServiceImpl(VacancyRepository vacancyRepository, DepartmentService departmentService, DepartmentRepository departmentRepository, PositionRepository positionRepository, PositionService positionService) {
        this.vacancyRepository = vacancyRepository;
        this.departmentService = departmentService;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.positionService = positionService;
    }

    @Override
    public Vacancy createVacancy(VacancyDto vacancyDto) {
        Position position = checkAndSetPosition(vacancyDto.getPosition());
        Department department = checkAndSetDepartment(vacancyDto.getDepartment());
        Vacancy vacancy = Vacancy.builder()
                .position(convertPosition(vacancyDto.getPosition()))
                .type(vacancyDto.getType())
                .department(convertDepartment((vacancyDto.getDepartment())))
                .lvl(convertLevel((vacancyDto.getLvl())))
                .description(vacancyDto.getDescriptions())
                .requirements(vacancyDto.getRequirements())
                .responsibilities(vacancyDto.getResponsibilities())
                .preferences(vacancyDto.getPreferences())
                .workingDays(vacancyDto.getWorkingDays())
                .workingHours(vacancyDto.getWorkingHours())
                .salary(vacancyDto.getSalary())
                .status(Status.OPEN)
                .createdDate(LocalDateTime.now())
                .createdUser(User.builder().id(1L).build())
                .build();

        return vacancyRepository.save(vacancy);
    }

    private Position checkAndSetPosition(String positionName) {
        Position position = new Position();
        if(positionRepository.findByName(positionName) == null) {
            autoFillPosition(positionName);
        }
        position = convertPosition(positionName);
        return position;
    }
    private void autoFillPosition(String newName) {
        Position newPosition = Position.builder()
                .name(newName)
                .build();
        positionService.addPosition(newPosition);
    }
    private Position convertPosition(String positionName) {
        Position position = positionRepository.findByName(positionName);
        return position;
    }

    private Department checkAndSetDepartment(String departmentName) {
        Department department = new Department();
        if(departmentRepository.findByName(departmentName) == null) {
            autoFillDepartment(departmentName);
        }
        department = convertDepartment(departmentName);
        return department;
    }
    private void autoFillDepartment(String newName) {
        DepartmentDto departmentDto = DepartmentDto.builder()
                .name(newName)
                .build();
        departmentService.createDepartment(departmentDto);
    }
    private Department convertDepartment(String departmentName) {
        Department department = departmentRepository.findByName(departmentName);
        return department;
    }

    private Level convertLevel(String levelName) {
        Level level = Level.valueOf(levelName.toUpperCase().replace(" ", "_"));
        return level;
    }

    @Override
    public List<VacancyDto> selectAllVacancy() {
        List<Vacancy> lists = vacancyRepository.findAll();
        List<VacancyDto> vacancies = new ArrayList<>();
        Iterator<Vacancy> itr = lists.iterator();
        while (itr.hasNext()){
            Vacancy vacancy = itr.next();
            VacancyDto dto = VacancyDto.builder()
                    .position(vacancy.getPosition().getName())
                    .type(vacancy.getType())
                    .descriptions(vacancy.getDescription())
                    .requirements(vacancy.getRequirements())
                    .responsibilities(vacancy.getResponsibilities())
                    .preferences(vacancy.getPreferences())
                    .workingDays(vacancy.getWorkingDays())
                    .workingHours(vacancy.getWorkingHours())
                    .salary(vacancy.getSalary())
                    .lvl(vacancy.getLvl().name())
                    .status(vacancy.getStatus())
                    .build();
            vacancies.add(dto);
        }
        return vacancies;
    }

    @Override
    public void updateVacancy(VacancyDto updatedVacancyDto) {
        Vacancy vacancy = vacancyRepository.findById(updatedVacancyDto.getId()).get();
        vacancy.setPosition(convertPosition(updatedVacancyDto.getPosition()));
        vacancy.setType(updatedVacancyDto.getType());
        vacancy.setDescription(updatedVacancyDto.getDescriptions());
        vacancy.setRequirements(updatedVacancyDto.getRequirements());
        vacancy.setResponsibilities(updatedVacancyDto.getResponsibilities());
        vacancy.setPreferences(updatedVacancyDto.getPreferences());
        vacancy.setWorkingDays(updatedVacancyDto.getWorkingDays());
        vacancy.setWorkingHours(vacancy.getWorkingHours());
        vacancy.setSalary(updatedVacancyDto.getSalary());
        vacancy.setLvl(vacancy.getLvl());
        vacancy.setStatus(updatedVacancyDto.getStatus());
        vacancyRepository.save(vacancy);
    }

    @Override
    public void deleteVacancy(long id) {
        vacancyRepository.deleteById(id);
    }
}
