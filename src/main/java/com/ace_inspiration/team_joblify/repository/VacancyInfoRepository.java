package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.VacancyInfo;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyInfoRepository extends DataTablesRepository<VacancyInfo, Long>,JpaRepository<VacancyInfo, Long> {


	@Query(value="SELECT\r\n"
			+ "   CAST(n.num AS char) AS month,\r\n"
			+ "    CAST(COALESCE(a.post_total, 0) AS char) AS post_total,\r\n"
			+ "    CAST(COALESCE(a.hired_post, 0) AS char) AS hired_post,\r\n"
			+ "    CAST(COALESCE(b.total_applied_candidate, 0) AS char) AS total_applied_candidate\r\n"
			+ "FROM (\r\n"
			+ "    SELECT 1 AS num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4\r\n"
			+ "    UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8\r\n"
			+ "    UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12\r\n"
			+ ") AS n\r\n"
			+ "LEFT JOIN (\r\n"
			+ "    SELECT\r\n"
			+ "        MONTH(vi.open_date) AS vi_month,\r\n"
			+ "        SUM(vi.required_post) AS post_total,\r\n"
			+ "        SUM(vi.hired_post) AS hired_post\r\n"
			+ "    FROM\r\n"
			+ "        vacancy_info vi\r\n"
			+ "    WHERE\r\n"
			+ "        vi.open_date >= :starDate AND vi.open_date <=:endDate\r\n"
			+ "    GROUP BY\r\n"
			+ "        MONTH(vi.open_date)\r\n"
			+ ") AS a\r\n"
			+ "ON n.num = a.vi_month\r\n"
			+ "LEFT JOIN (\r\n"
			+ "    SELECT\r\n"
			+ "        MONTH(vi.open_date) AS vi_month,\r\n"
			+ "        COUNT(c.id) AS total_applied_candidate\r\n"
			+ "    FROM\r\n"
			+ "        vacancy_info vi\r\n"
			+ "    JOIN\r\n"
			+ "        candidate c ON c.vacancy_info_id = vi.id\r\n"
			+ "    WHERE\r\n"
			+ "        vi.open_date >= :starDate AND vi.open_date <= :endDate\r\n"
			+ "    GROUP BY\r\n"
			+ "        MONTH(vi.open_date)\r\n"
			+ ") AS b\r\n"
			+ "ON n.num = b.vi_month;\r\n", nativeQuery = true)
    List<Object[]> getVacancyInfoWithCandidateCounts(String starDate,String endDate);
    
@Query(value="SELECT DISTINCT YEAR(open_date) AS min_year\r\n"
		+ "FROM vacancy_info;\r\n"
		, nativeQuery = true)
List<Object[]> getYear();

    @Query("SELECT vd FROM VacancyInfo vd ORDER BY vd.id DESC LIMIT 3")
    List<VacancyInfo> getLastVacancyInfo();
    List<VacancyInfo> findAllByOrderByIdDesc();


}