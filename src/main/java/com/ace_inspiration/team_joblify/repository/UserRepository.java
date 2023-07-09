package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Interview;
import com.ace_inspiration.team_joblify.entity.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends DataTablesRepository<User, Long> {

}
