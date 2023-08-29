package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.VacancyView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyViewRepository extends DataTablesRepository<VacancyView,Long> {

    @Procedure(name = "vacancy_filter")
    Page<VacancyView> filterVacancies(@Param("sort_by") String sortBy,
                                      @Param("title") String title,
                                      @Param("time_range") String timeRange,
                                      @Param("type") String type,
                                      @Param("levels") String levels,
                                      @Param("under_10") boolean under10,
                                      @Param("including_closed") boolean includingClosed,
                                      Pageable pageable);

    @Query("SELECT vv FROM VacancyView vv WHERE vv.status = 'OPEN' ORDER BY vv.updatedTime DESC LIMIT 3")
    List<VacancyView> getLastVacancyView();
    int countBy();

    @Query("SELECT v FROM VacancyView v WHERE (v.position = :position OR v.department = :department) AND v.status = 'OPEN'")
    List<VacancyView> findVacancyViewByPositionAndDepartmentAndStatus(
            @Param("position") String position,
            @Param("department") String department
    );

}
