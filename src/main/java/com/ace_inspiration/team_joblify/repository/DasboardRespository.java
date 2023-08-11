package com.ace_inspiration.team_joblify.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ace_inspiration.team_joblify.entity.Dasboard;

@Repository
public interface DasboardRespository extends DataTablesRepository<Dasboard, Long>{

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
