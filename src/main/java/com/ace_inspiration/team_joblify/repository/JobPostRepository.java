package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.JobPost;
import com.ace_inspiration.team_joblify.entity.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostRepository extends DataTablesRepository<Long, JobPost> {
}
