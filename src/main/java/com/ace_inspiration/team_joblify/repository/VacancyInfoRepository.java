package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.VacancyInfo;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyInfoRepository extends DataTablesRepository<VacancyInfo, Long>,JpaRepository<VacancyInfo, Long> {

    @Query("SELECT vd FROM VacancyInfo vd ORDER BY vd.id DESC LIMIT 3")
    List<VacancyInfo> getLastVacancyDepartments();
    List<VacancyInfo> findAllByOrderByIdDesc();

}
