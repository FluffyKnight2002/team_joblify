package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Role;
import com.ace_inspiration.team_joblify.entity.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends DataTablesRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByRole(Role role);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
   Optional<User> findByUsernameAndIdNot(String username, long userId);
   Optional<User> findByEmailAndIdNot(String email, long userId);
    Optional<User> findByPhoneAndIdNot(String phone, long userId);
}

