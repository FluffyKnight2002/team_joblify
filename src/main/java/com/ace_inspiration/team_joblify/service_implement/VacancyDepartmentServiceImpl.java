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
    public VacancyInfo createdVacancyDepartments(VacancyDto vacancyDto) {
        VacancyInfo vacancyInfo = dtoToEntity(vacancyDto);
        return vacancyDepartmentRepository.save(vacancyInfo);
    }

    @Override
    public List<VacancyDto> selectAllVacancyDepartments() {
        List<VacancyDto> vacancyDtos = new ArrayList<>();
        List<VacancyInfo> vacancyInfos = vacancyDepartmentRepository.findAll();
        for(VacancyInfo vacancyInfo : vacancyInfos) {
            VacancyDto vacancyDto = entityToDto(vacancyInfo);
            vacancyDtos.add(vacancyDto);
        }
        return vacancyDtos;
    }

    @Override
    public List<VacancyDto> selectLastVacancies() {
        List<VacancyInfo> lastVacancies = vacancyDepartmentRepository.getLastVacancyDepartments();
        List<VacancyDto> vacancyDtos = new ArrayList<>();
        for(VacancyInfo vacancyInfo : lastVacancies) {
            VacancyDto vacancyDto = entityToDto(vacancyInfo);
            vacancyDtos.add(vacancyDto);
        }
        return vacancyDtos;
    }

    @Override
    public VacancyDto selectVacancyById(long id) {
        VacancyDto vacancyDto = new VacancyDto();
        Optional<VacancyInfo> optionalVacancyDepartment = vacancyDepartmentRepository.findById(id);
        if(optionalVacancyDepartment.isPresent()) {
            vacancyDto = entityToDto(optionalVacancyDepartment.get());
        }

        return vacancyDto;
    }

    @Override
    public VacancyInfo updateVacancyDepartments(VacancyDto vacancyDto) {
        return null;
    }

    @Override
    public void closedVacancyDepartments(long id) {

    }

    @Override
    public Page<VacancyDto> getPaginatedVacancies(Pageable pageable) {
        Page<VacancyInfo> vacanciesPage = vacancyDepartmentRepository.findAll(pageable);

        List<VacancyDto> vacancyDtos = vacanciesPage.getContent().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(vacancyDtos, pageable, vacanciesPage.getTotalElements());
    }

    private VacancyInfo dtoToEntity(VacancyDto vacancyDto) {
        return VacancyInfo.builder()
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
    }

    public VacancyDto entityToDto(VacancyInfo vacancyInfo) {
        VacancyDto vacancyDto = new VacancyDto();
        System.out.println(vacancyInfo.getVacancy().getPosition().getName());
        Vacancy vacancy = vacancyRepository.findById(vacancyInfo.getId()).get();
        vacancyDto.setId(vacancyInfo.getId());
        vacancyDto.setPosition(vacancyInfo.getVacancy().getPosition().getName());
        vacancyDto.setDepartment(vacancyInfo.getVacancy().getDepartment().getName());
        vacancyDto.setAddress(vacancyInfo.getVacancy().getAddress().getName());
        vacancyDto.setDescriptions(vacancyInfo.getDescription());
        vacancyDto.setResponsibilities(vacancyInfo.getResponsibilities());
        vacancyDto.setRequirements(vacancyInfo.getRequirements());
        vacancyDto.setPreferences(vacancyInfo.getPreferences());
        vacancyDto.setOnSiteOrRemote(String.valueOf(vacancyInfo.getOnSiteOrRemote()));
        vacancyDto.setWorkingHours(vacancyInfo.getWorkingHours());
        vacancyDto.setWorkingDays(vacancyInfo.getWorkingDays());
        vacancyDto.setType(vacancyInfo.getJobType());
        vacancyDto.setLvl(String.valueOf(vacancyInfo.getLvl()));
        vacancyDto.setPost(vacancyInfo.getPost());
        vacancyDto.setSalary(vacancyInfo.getSalary());
        vacancyDto.setNote(vacancyInfo.getNote());
        // Fetch updated user's username if it exists
        if (vacancyInfo.getUpdatedUser() != null) {
            Long updatedUserId = vacancyInfo.getUpdatedUser().getId();
            Optional<User> updatedUserOptional = userRepository.findById(updatedUserId);
            if (updatedUserOptional.isPresent()) {
                vacancyDto.setUpdatedUsername(updatedUserOptional.get().getUsername());
            }
        }
        vacancyDto.setCreadedUsername(vacancyInfo.getVacancy().getCreatedUser().getUsername());
        vacancyDto.setCreatedDateTime(vacancyInfo.getVacancy().getCreatedUser().getCreatedDate());
        vacancyDto.setUpdatedUsername(vacancyInfo.getUpdatedUser().getUsername());
        vacancyDto.setUpdatedTime(vacancyInfo.getUpdatedTime());
        vacancyDto.setOpenDate(vacancyInfo.getOpenDate());
        vacancyDto.setCloseDate(vacancyInfo.getCloseDate());
        vacancyDto.setStatus(String.valueOf(vacancyInfo.getVacancy().getStatus()));

        return vacancyDto;
    }

    private Level convertLevel(String levelName) {
        return Level.valueOf(levelName);
    }

    private OnSiteOrRemote convertOnSiteOrRemote(String onSiteOrRemote) {
        return OnSiteOrRemote.valueOf(onSiteOrRemote);
    }
}
