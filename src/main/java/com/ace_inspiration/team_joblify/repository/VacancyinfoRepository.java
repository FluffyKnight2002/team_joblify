package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.dto.CountDto;
import com.ace_inspiration.team_joblify.entity.VacancyInfo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyinfoRepository extends JpaRepository<VacancyInfo, Long> {
	
	@Query(value = "SELECT " +
            "    vi.id AS id, " +
            "    vi.close_date AS close, " +
            "    vi.open_date AS open, " +
            "    vi.post AS post_total, " +
            "    vi.hired_post AS hired, " +
            "    COUNT(c.id) AS total_candidates " +
            "FROM " +
            "    vacancy_info vi " +
            "JOIN " +
            "    candidate c ON (c.vacancy_info_id = vi.id) " +
            "GROUP BY " +
            "    vi.id", nativeQuery = true)
	 List<Object[]> getVacancyInfoWithCandidateCounts();
}
