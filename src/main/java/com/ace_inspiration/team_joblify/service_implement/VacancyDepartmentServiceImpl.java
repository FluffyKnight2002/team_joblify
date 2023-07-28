package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.*;
import com.ace_inspiration.team_joblify.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacancyDepartmentServiceImpl implements VacancyDepartmentService {

    private final VacancyDepartmentRepository vacancyDepartmentRepository;
    private final VacancyService vacancyService;
    private final VacancyRepository vacancyRepository;
    private final PositionRepository positionRepository;
    private final PositionService positionService;
    private final DepartmentRepository departmentRepository;
    private final DepartmentService departmentService;
    private final AddressRepository addressRepository;
    private final AddressService addressService;
    private final UserRepository userRepository;

    @Override
    public VacancyDepartment createdVacancyDepartments(VacancyDto vacancyDto) {
        VacancyDepartment vacancyDepartment = dtoToEntity(vacancyDto);
        return vacancyDepartmentRepository.save(vacancyDepartment);
    }

    @Override
    public List<VacancyDto> selectAllVacancyDepartments() {
        List<VacancyDto> vacancyDtos = new ArrayList<>();
        List<VacancyDepartment> vacancyDepartments = vacancyDepartmentRepository.findAll();
        for(VacancyDepartment vacancyDepartment: vacancyDepartments) {
            VacancyDto vacancyDto = entityToDto(vacancyDepartment);
            vacancyDtos.add(vacancyDto);
        }
        return vacancyDtos;
    }

    @Override
    public List<VacancyDto> selectLastVacancies() {
        List<VacancyDepartment> lastVacancies = vacancyDepartmentRepository.getLastVacancyDepartments();
        List<VacancyDto> vacancyDtos = new ArrayList<>();
        for(VacancyDepartment vacancyDepartment: lastVacancies) {
            VacancyDto vacancyDto = entityToDto(vacancyDepartment);
            vacancyDtos.add(vacancyDto);
        }
        return vacancyDtos;
    }

    @Override
    public VacancyDto selectVacancyById(long id) {
        VacancyDto vacancyDto = new VacancyDto();
        Optional<VacancyDepartment> optionalVacancyDepartment = vacancyDepartmentRepository.findById(id);
        if(optionalVacancyDepartment.isPresent()) {
            vacancyDto = entityToDto(optionalVacancyDepartment.get());
        }

        return vacancyDto;
    }

    @Override
    public VacancyDepartment updateVacancyDepartments(VacancyDto vacancyDto) {
        return null;
    }

    @Override
    public void closedVacancyDepartments(long id) {

    }

    @Override
    public Page<VacancyDto> getPaginatedVacancies(Pageable pageable) {
        Page<VacancyDepartment> vacanciesPage = vacancyDepartmentRepository.findAll(pageable);

        List<VacancyDto> vacancyDtos = vacanciesPage.getContent().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(vacancyDtos, pageable, vacanciesPage.getTotalElements());
    }

    private VacancyDepartment dtoToEntity(VacancyDto vacancyDto) {
        VacancyDepartment vacancyDepartment = VacancyDepartment.builder()
                .vacancy(vacancyService.createVacancy(vacancyDto))
                .description(vacancyDto.getDescriptions())
                .responsibilities(vacancyDto.getResponsibilities())
                .requirements(vacancyDto.getRequirements())
                .preferences(vacancyDto.getPreferences())
                .onSiteOrRemote(convertOnSiteOrRemote(vacancyDto.getOnSiteOrRemote()))
                .hiredPost(0)
                .workingHours(vacancyDto.getWorkingHours())
                .workingDays(vacancyDto.getWorkingDays())
                .jobType(vacancyDto.getType())
                .lvl(convertLevel(vacancyDto.getLvl()))
                .post(vacancyDto.getPost())
                .salary(vacancyDto.getSalary())
                .updatedUser(User.builder().id(1L).build())
                .updatedTime(LocalDateTime.now())
                .openDate(LocalDate.now())
                .closeDate(LocalDate.now())
                .note(vacancyDto.getNote())
                .build();
        return vacancyDepartment;
    }

    public VacancyDto entityToDto(VacancyDepartment vacancyDepartment) {
        VacancyDto vacancyDto = new VacancyDto();
        System.out.println(vacancyDepartment.getVacancy().getPosition().getName());
        Vacancy vacancy = vacancyRepository.findById(vacancyDepartment.getId()).get();
        vacancyDto.setId(vacancyDepartment.getId());
        vacancyDto.setPosition(vacancyDepartment.getVacancy().getPosition().getName());
        vacancyDto.setDepartment(vacancyDepartment.getVacancy().getDepartment().getName());
        vacancyDto.setAddress(vacancyDepartment.getVacancy().getAddress().getName());
        vacancyDto.setDescriptions(vacancyDepartment.getDescription());
        vacancyDto.setResponsibilities(vacancyDepartment.getResponsibilities());
        vacancyDto.setRequirements(vacancyDepartment.getRequirements());
        vacancyDto.setPreferences(vacancyDepartment.getPreferences());
        vacancyDto.setOnSiteOrRemote(String.valueOf(vacancyDepartment.getOnSiteOrRemote()));
        vacancyDto.setWorkingHours(vacancyDepartment.getWorkingHours());
        vacancyDto.setWorkingDays(vacancyDepartment.getWorkingDays());
        vacancyDto.setType(vacancyDepartment.getJobType());
        vacancyDto.setLvl(String.valueOf(vacancyDepartment.getLvl()));
        vacancyDto.setPost(vacancyDepartment.getPost());
        vacancyDto.setSalary(vacancyDepartment.getSalary());
        vacancyDto.setNote(vacancyDepartment.getNote());
        // Fetch updated user's username if it exists
        if (vacancyDepartment.getUpdatedUser() != null) {
            Long updatedUserId = vacancyDepartment.getUpdatedUser().getId();
            Optional<User> updatedUserOptional = userRepository.findById(updatedUserId);
            if (updatedUserOptional.isPresent()) {
                vacancyDto.setUpdatedUsername(updatedUserOptional.get().getUsername());
            }
        }
        vacancyDto.setCreadedUsername(vacancyDepartment.getVacancy().getCreatedUser().getUsername());
        vacancyDto.setCreatedDateTime(vacancyDepartment.getVacancy().getCreatedUser().getCreatedDate());
        vacancyDto.setUpdatedUsername(vacancyDepartment.getUpdatedUser().getUsername());
        vacancyDto.setUpdatedTime(vacancyDepartment.getUpdatedTime());
        vacancyDto.setOpenDate(vacancyDepartment.getOpenDate());
        vacancyDto.setCloseDate(vacancyDepartment.getCloseDate());
        vacancyDto.setStatus(String.valueOf(vacancyDepartment.getVacancy().getStatus()));

        return vacancyDto;
    }

    private Level convertLevel(String levelName) {
        Level level = Level.valueOf(levelName);
        return level;
    }

    private OnSiteOrRemote convertOnSiteOrRemote(String onSiteOrRemote) {
        OnSiteOrRemote chgOnSiteOrRemote  = OnSiteOrRemote.valueOf(onSiteOrRemote);
        return chgOnSiteOrRemote;
    }
}
