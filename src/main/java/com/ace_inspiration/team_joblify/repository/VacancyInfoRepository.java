package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.VacancyInfo;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyInfoRepository extends DataTablesRepository<VacancyInfo, Long>,JpaRepository<VacancyInfo, Long> {
    @Query(value = "SELECT " +
            "    vi.id AS id, " +
            "    vi.close_date AS close, " +
            "    vi.open_date AS open, " +
            "    vi.required_post AS post_total, " +
            "    vi.hired_post AS hired, " +
            "    COUNT(c.id) AS total_candidates " +
            "FROM " +
            "    vacancy_info vi " +
            "JOIN " +
            "    candidate c ON (c.vacancy_info_id = vi.id) " +
            "GROUP BY " +
            "    vi.id", nativeQuery = true)
    List<Object[]> getVacancyInfoWithCandidateCounts();
    @Query("SELECT vd FROM VacancyInfo vd ORDER BY vd.id DESC LIMIT 3")
    List<VacancyInfo> getLastVacancyDepartments();
    List<VacancyInfo> findAllByOrderByIdDesc();

}