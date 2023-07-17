package com.ace_inspiration.team_joblify.dto;

import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.entity.VacancyDepartment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDto {

    private long id;
    private String name;
    private String note;
    private List<User> user=new ArrayList<>();
    private List<VacancyDepartment> vacancyDepartment =new ArrayList<>();

}
