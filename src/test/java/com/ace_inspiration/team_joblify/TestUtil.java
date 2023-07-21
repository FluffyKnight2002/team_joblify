package com.ace_inspiration.team_joblify;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.*;

import java.time.LocalDate;
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

    public static Vacancy createMockVacancy() {
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
                .address(address)
                .department(createMockDepartment())
                .createdUser(User.builder().build())
                .createdDate(LocalDateTime.now())
                .build();
        vacancy.setCreatedDate(LocalDateTime.now());
        // Set other properties accordingly
        return vacancy;
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
        vacancyDto.setSalary("3 Lakhs");
        vacancyDto.setStatus(Status.OPEN);
        // Set other properties accordingly
        return vacancyDto;
    }

    public static List<Vacancy> createMockVacancyList() {
        List<Vacancy> vacancies = new ArrayList<>();
        vacancies.add(createMockVacancy());
        return vacancies;
    }
    public static VacancyDepartment createMockVacancyDepartment() {
        VacancyDepartment vacancyDepartment = VacancyDepartment.builder()
                .id(1L)
                .post(2)
                .jobType("Mock Department")
                .vacancy(createMockVacancy())
                .lvl(Level.ENTRY_LEVEL)
                .salary("Mock salary")
                .updatedUser(createMockUser())
                .updatedTime(LocalDateTime.now())
                .openDate(LocalDate.now())
                .closeDate(LocalDate.now())
                .note("Mock note")
                .build();

        return vacancyDepartment;
    }
}

