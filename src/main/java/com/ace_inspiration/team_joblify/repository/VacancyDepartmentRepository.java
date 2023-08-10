package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Vacancyinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyDepartmentRepository extends JpaRepository<Vacancyinfo, Long> {
}
