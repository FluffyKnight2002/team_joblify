package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Skills;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface SkillsRepository extends DataTablesRepository<Skills, Long> {
}
