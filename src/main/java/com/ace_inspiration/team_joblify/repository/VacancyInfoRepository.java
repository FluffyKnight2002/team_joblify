package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.VacancyInfo;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
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
    @Query("SELECT vd, COUNT(c) AS applicants " +
            "FROM VacancyInfo vd " +
            "JOIN FETCH vd.vacancy v " +
            "JOIN FETCH v.position " +
            "JOIN FETCH v.department " +
            "JOIN FETCH v.address " +
            "LEFT JOIN vd.candidates c " +
            "GROUP BY vd")
    List<Object[]> findAllWithDataTablesAndApplicantsCount(DataTablesInput input);

}
