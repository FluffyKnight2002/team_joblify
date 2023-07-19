package com.ace_inspiration.team_joblify;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.Address;
import com.ace_inspiration.team_joblify.entity.Status;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.dto.DepartmentDto;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MockVacancyDto {

    public static VacancyDto createMockVacancyDto() {
        Address address = Address.builder()
                .id(1l)
                .name("Mock Address")
                .build();
        VacancyDto vacancyDto = VacancyDto.builder()
                .id(1L)
                .position("Mock position")
                .post(2)
                .requiredPost(1)
                .applicants(3)
                .type("Mock type")
                .department("Mock department")
                .descriptions("Mock description")
                .requirements("Mock requirements")
                .preferences("Mock preferences")
                .responsibilities("Mock responsibilities")
                .address(address.getName())
                .workingDays("Mock working day")
                .workingHours("Mock working hour")
                .lvl("Entry Level")
                .salary("Mock salary")
                .status(Status.OPEN)
                .departmentDto(createMockDepartmentDto())
                .updatedUser(createMockUser())
                .creadedUser(createMockUser())
                .createdDateTime(LocalDateTime.now())
                .openDate(LocalDate.now())
                .closeDate(LocalDate.now())
                .note("Mock note")
                .build();

        return vacancyDto;
    }

    private static DepartmentDto createMockDepartmentDto() {
        DepartmentDto departmentDto = DepartmentDto.builder()
                .name("Mock department name")
                .build();

        return departmentDto;
    }

    private static User createMockUser() {
        User user = new User();
        // Set user properties accordingly
        return user;
    }
}

