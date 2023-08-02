package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.*;
import com.ace_inspiration.team_joblify.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class VacancyInfoServiceImpl implements VacancyInfoService {

    private final VacancyInfoRepository vacancyInfoRepository;
    private final VacancyViewRepository vacancyViewRepository;
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
    public VacancyInfo createdVacancyInfo(VacancyDto vacancyDto) {
        VacancyInfo vacancyInfo = dtoToEntity(vacancyDto);
        return vacancyInfoRepository.save(vacancyInfo);
    }

    @Override
    public List<VacancyDto> selectAllVacancyInfo() {
        List<VacancyDto> vacancyDtos = new ArrayList<>();
        List<VacancyInfo> vacancyInfos = vacancyInfoRepository.findAll();
        for(VacancyInfo vacancyInfo : vacancyInfos) {
            VacancyDto vacancyDto = entityToDto(vacancyInfo);
            vacancyDtos.add(vacancyDto);
        }
        return vacancyDtos;
    }

    @Override
    public List<VacancyDto> selectLastVacancies() {
        List<VacancyInfo> lastVacancies = vacancyInfoRepository.getLastVacancyDepartments();
        List<VacancyDto> vacancyDtos = new ArrayList<>();
        for(VacancyInfo vacancyInfo : lastVacancies) {
            VacancyDto vacancyDto = entityToDto(vacancyInfo);
            vacancyDtos.add(vacancyDto);
        }
        return vacancyDtos;
    }

    @Override
    public DataTablesOutput<VacancyView> getVacanciesDataTable(DataTablesInput input) {
        return vacancyViewRepository.findAll(input);
    }

    @Override
    public VacancyDto selectVacancyById(long id) {
        VacancyDto vacancyDto = new VacancyDto();
        Optional<VacancyInfo> optionalVacancyDepartment = vacancyInfoRepository.findById(id);
        if(optionalVacancyDepartment.isPresent()) {
            vacancyDto = entityToDto(optionalVacancyDepartment.get());
        }

        return vacancyDto;
    }

    @Override
    public VacancyInfo updateVacancyInfo(VacancyDto vacancyDto) {
        Long vacancyId = vacancyDto.getId();
        VacancyInfo existingVacancy = null;

        existingVacancy = vacancyInfoRepository.findById(vacancyId).orElse(null);
        // Update the properties of the existingVacancy based on vacancyDto
        existingVacancy.setVacancy(vacancyService.updateVacancy(vacancyDto));
        existingVacancy.setDescription(vacancyDto.getDescriptions());
        existingVacancy.setRequirements(vacancyDto.getRequirements());
        existingVacancy.setResponsibilities(vacancyDto.getResponsibilities());
        existingVacancy.setPreferences(vacancyDto.getPreferences());
        existingVacancy.setWorkingDays(vacancyDto.getWorkingDays());
        existingVacancy.setWorkingHours(vacancyDto.getWorkingHours());
        existingVacancy.setSalary(vacancyDto.getSalary());
        existingVacancy.setPost(vacancyDto.getPost());
        existingVacancy.setJobType(convertJobType(vacancyDto.getType()));
        existingVacancy.setUpdatedUser(userRepository.findById(vacancyDto.getUpdatedUserId()).get());
        existingVacancy.setUpdatedTime(LocalDateTime.now());
        existingVacancy.setLvl(convertLevel(vacancyDto.getLvl()));
        existingVacancy.setOnSiteOrRemote(convertOnSiteOrRemote(vacancyDto.getOnSiteOrRemote()));
        existingVacancy.setNote(vacancyDto.getNote());

        // Save the updated or new entity
        return vacancyInfoRepository.save(existingVacancy);
    }

    @Override
    public void closedVacancyDepartments(long id) {

    }

    @Override
    public Page<VacancyDto> getPaginatedVacancies(Pageable pageable) {
        Page<VacancyInfo> vacanciesPage = vacancyInfoRepository.findAll(pageable);
        List<VacancyDto> vacancyDtos = vacanciesPage.getContent().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(vacancyDtos, pageable, vacanciesPage.getTotalElements());
    }

    // The scheduled task will be executed every day at 12:00 PM (noon)
    @Override
    @Scheduled(cron = "0 0 12 * * ?")
    public void updateStatusAfter30Days() {
        List<VacancyInfo> vacancyInfos = vacancyInfoRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (VacancyInfo vacancyInfo : vacancyInfos) {
            LocalDate openDate = vacancyInfo.getOpenDate();
            long daysDifference = ChronoUnit.DAYS.between(openDate, currentDate);

            if (daysDifference >= 30) {
                vacancyInfo.getVacancy().setStatus(Status.CLOSED);
                vacancyInfoRepository.save(vacancyInfo);
            }
        }
    }

    @Override
    public boolean closeVacancyById(long id) {
        Optional<VacancyInfo> optionalVacancyInfo = vacancyInfoRepository.findById(id);
        if(optionalVacancyInfo.isPresent()){
            VacancyInfo vacancyInfo = optionalVacancyInfo.get();
            vacancyInfo.getVacancy().setStatus(Status.CLOSED);
            vacancyInfoRepository.save(vacancyInfo);
        }
        return false;
    }

    private VacancyInfo dtoToEntity(VacancyDto vacancyDto) {
        VacancyInfo vacancyInfo = VacancyInfo.builder()
                .vacancy(vacancyService.createVacancy(vacancyDto))
                .description(vacancyDto.getDescriptions())
                .responsibilities(vacancyDto.getResponsibilities())
                .requirements(vacancyDto.getRequirements())
                .preferences(vacancyDto.getPreferences())
                .onSiteOrRemote(convertOnSiteOrRemote(vacancyDto.getOnSiteOrRemote()))
                .hiredPost(0)
                .workingHours(vacancyDto.getWorkingHours())
                .workingDays(vacancyDto.getWorkingDays())
                .jobType(convertJobType(vacancyDto.getType()))
                .lvl(convertLevel(vacancyDto.getLvl()))
                .post(vacancyDto.getPost())
                .salary(vacancyDto.getSalary())
                .updatedUser(userRepository.findById(vacancyDto.getUpdatedUserId()).get())
                .updatedTime(LocalDateTime.now())
                .openDate(LocalDate.now())
                .closeDate(LocalDate.now())
                .note(vacancyDto.getNote())
                .build();
        return vacancyInfo;
    }

    public VacancyDto entityToDto(VacancyInfo vacancyInfo) {
        VacancyDto vacancyDto = new VacancyDto();
//        Vacancy vacancy = vacancyRepository.findById(vacancyDepartment.getId()).get();
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
        vacancyDto.setType(String.valueOf(vacancyInfo.getJobType()));
        vacancyDto.setLvl(String.valueOf(vacancyInfo.getLvl()));
        vacancyDto.setPost(vacancyInfo.getPost());
        vacancyDto.setSalary(vacancyInfo.getSalary());
        String note = (vacancyInfo.getNote() == null) ? "There is no note." : vacancyInfo.getNote();
        vacancyDto.setNote(note);
        // Fetch updated user's username if it exists
//        if (vacancyDepartment.getUpdatedUser() != null) {
//            Long updatedUserId = vacancyDepartment.getUpdatedUser().getId();
//            Optional<User> updatedUserOptional = userRepository.findById(updatedUserId);
//            if (updatedUserOptional.isPresent()) {
//                vacancyDto.setUpdatedUsername(updatedUserOptional.get().getUsername());
//            }
//        }
        vacancyDto.setCreatedUsername(vacancyInfo.getVacancy().getCreatedUser().getUsername());
        vacancyDto.setCreatedDateTime(vacancyInfo.getVacancy().getCreatedUser().getCreatedDate());
        vacancyDto.setUpdatedUsername(vacancyInfo.getUpdatedUser().getUsername());
        vacancyDto.setUpdatedTime(vacancyInfo.getUpdatedTime());
        vacancyDto.setOpenDate(vacancyInfo.getOpenDate());
        vacancyDto.setCloseDate(vacancyInfo.getCloseDate());
        vacancyDto.setStatus(String.valueOf(vacancyInfo.getVacancy().getStatus()));
        System.out.println(vacancyDto.getPosition());
        return vacancyDto;
    }

    public VacancyDto tupleToVacancyDto(Object[] tuple) {
        VacancyInfo vacancyInfo = (VacancyInfo) tuple[0];
        Long applicantsCount = (Long) tuple[1];

        VacancyDto vacancyDto = entityToDto(vacancyInfo);
        vacancyDto.setApplicants(Integer.valueOf(Math.toIntExact(applicantsCount)));

        return vacancyDto;
    }

    private Level convertLevel(String levelName) {
        Level level = Level.valueOf(levelName);
        return level;
    }

    private JobType convertJobType(String jobTypeName){
        JobType jobType = JobType.valueOf(jobTypeName);
        return jobType;
    }

    private OnSiteOrRemote convertOnSiteOrRemote(String onSiteOrRemote) {
        OnSiteOrRemote chgOnSiteOrRemote  = OnSiteOrRemote.valueOf(onSiteOrRemote);
        return chgOnSiteOrRemote;
    }

}
