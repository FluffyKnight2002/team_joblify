package com.ace_inspiration.team_joblify;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static User createMockUser() {
        User user = new User();

        // Set mock values for the User fields
        user.setId(1L);
        user.setUsername("mockUsername");
        user.setName("mockName");
        user.setEmail("mockEmail");
        user.setPhone("mockPhNo");
        user.setGender(Gender.FEMALE);
        user.setAddress("mockAddress");
        user.setPhoto("mockImage");
        user.setPassword("mockPassword");
        user.setRole(Role.DEFAULT_HR);
        user.setCreatedDate(LocalDateTime.now());
        user.setLastUpdatedDate(LocalDateTime.now());
        user.setNote("MockNote");
        user.setCreatedVacancies(new ArrayList<>());
        user.setUpdatedJobPosts(new ArrayList<>());
        user.setDepartment(new Department());

        return user;
    }

    public static Position createMockPosition() {
        Position position = new Position();
        position.setId(1L);
        position.setName("Mock position");
        return position;
    }

    public static Department createMockDepartment() {
        Department department = Department.builder()
                .id(1L)
                .name("Sales")
                .user(new ArrayList<>())
                .build();
        return department;
    }

    public static Department createMockDepartmentDto(){
        Department departmentDto = Department.builder()
                .id(1L)
                .name("Sales")
                .user(new ArrayList<>())
                .build();
        return departmentDto;
    }

    public static List<Department> createMockDepartmentList() {
        List<Department> departments = new ArrayList<>();
        departments.add(createMockDepartment());
        return departments;
    }

    public static VacancyInfo createMockVacancyInfo() {
        Address address = Address.builder()
                .id(1l)
                .name("Mock Address")
                .build();
        Position position = Position.builder()
                .id(1l)
                .name("Back-end Developer")
                .build();
        Department department = Department.builder()
                .id(1l)
                .name("Mock Department")
                .build();
        Vacancy vacancy = Vacancy.builder()
                .id(1L)
                .position(position)
                .department(department)
                .department(createMockDepartment())
                .createdUser(User.builder().build())
                .createdDate(LocalDateTime.now())
                .build();
        vacancy.setCreatedDate(LocalDateTime.now());
        // Set other properties accordingly

        VacancyInfo vacancyInfo = VacancyInfo.builder()
                .id(1L)
                .vacancy(vacancy)
                .address(address)
                .status(Status.OPEN)
                .jobType(JobType.FULL_TIME)
                .note("MOCK NOTE")
                .lvl(Level.ENTRY_LEVEL)
                .salary(500000)
                .updatedUser(User.builder().id(1L).name("Admin").build())
                .openDate(LocalDateTime.now())
                .closeDate(LocalDateTime.now().plusDays(30))
                .post(2)
                .updatedTime(LocalDateTime.now())
                .description("Mock description")
                .responsibilities("Mock responsibilities")
                .requirements("Mock requirements")
                .preferences("Mock preferences")
                .hiredPost(0)
                .onSiteOrRemote(OnSiteOrRemote.ON_SITE)
                .workingDays("Mon ~ Fri")
                .workingHours("9:00 AM ~ 5:00 PM")
                .build();

        return vacancyInfo;
    }

    public static VacancyDto createMockVacancyDto() {
        VacancyDto vacancyDto = new VacancyDto();
        vacancyDto.setId(1L);
        vacancyDto.setPosition("Back-end Developer");
        vacancyDto.setPost(1);
        vacancyDto.setType("Mock type");
        vacancyDto.setDepartment("Banking");
        vacancyDto.setDescriptions("Mock description");
        vacancyDto.setRequirements("Mock requirements");
        vacancyDto.setPreferences("Mock preferences");
        vacancyDto.setResponsibilities("Mock responsibilities");
        vacancyDto.setAddress("Mock address");
        vacancyDto.setWorkingDays("Mock working day");
        vacancyDto.setWorkingHours("Mock working hour");
        vacancyDto.setLvl("Entry Level");
        vacancyDto.setSalary(500000);
        vacancyDto.setStatus("OPEN");
        // Set other properties accordingly
        return vacancyDto;
    }

    public static List<VacancyInfo> createMockVacancyList() {
        List<VacancyInfo> vacanciesInfo = new ArrayList<>();
        vacanciesInfo.add(createMockVacancyInfo());
        return vacanciesInfo;
    }
//    public static VacancyInfo createMockVacancyDepartment() {
//        VacancyInfo vacancyInfo = VacancyInfo.builder()
//                .id(1L)
//                .post(2)
//                .jobType(JobType.FULL_TIME)
//                .vacancy(createVacancy())
//                .lvl(Level.ENTRY_LEVEL)
//                .salary(500000)
//                .updatedUser(createMockUser())
//                .updatedTime(LocalDateTime.now())
//                .openDate(LocalDateTime.now())
//                .closeDate(LocalDateTime.now())
//                .note("Mock note")
//                .build();
//
//        return vacancyInfo;
//    }
}

