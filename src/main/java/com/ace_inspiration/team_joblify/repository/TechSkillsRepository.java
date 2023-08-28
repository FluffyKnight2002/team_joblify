package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.TechSkills;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import java.util.List;
import java.util.Optional;

public interface TechSkillsRepository extends DataTablesRepository<TechSkills, Long> {

    Optional<TechSkills> findByNameIgnoreCase(String name);
//    List<TechSkills> findTechSkillsIdBySummaryId(long id);
}
