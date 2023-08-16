package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.dto.VacancyFilteredDto;
import com.ace_inspiration.team_joblify.entity.VacancyInfo;
import com.ace_inspiration.team_joblify.entity.VacancyView;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
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
    List<VacancyInfo> getLastVacancyInfo();
    List<VacancyInfo> findAllByOrderByIdDesc();

//    @Query(nativeQuery = true, value = "CALL vacancy_filter(:sort_by, :including_closed, :title, :type, :levels, :under_10, :time_range, :page, :page_size)")
//            List<Object[]> vacancyFilter(
//            @Param("sort_by") String sortBy,
//            @Param("including_closed") boolean includingClosed,
//            @Param("title") String title,
//            @Param("type") String type,
//            @Param("levels") String levels,
//            @Param("under_10") boolean under10,
//            @Param("time_range") String timeRange,
//            @Param("page") int page,
//            @Param("page_size") int pageSize);

}