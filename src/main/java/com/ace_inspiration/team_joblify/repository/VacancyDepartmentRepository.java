package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.VacancyDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyDepartmentRepository extends JpaRepository<VacancyDepartment, Long> {

    @Query("SELECT vd FROM VacancyDepartment vd ORDER BY vd.id DESC LIMIT 3")
    List<VacancyDepartment> getLastVacancyDepartments();

}
