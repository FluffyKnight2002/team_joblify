package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.LanguageSkills;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import java.util.List;
import java.util.Optional;

public interface LanguageSkillsRepository extends DataTablesRepository<LanguageSkills, Long> {

    Optional<LanguageSkills> findByNameIgnoreCase(String name);
//    List<LanguageSkills> findLanguageSkillIdBySummaryId(long id);
}
