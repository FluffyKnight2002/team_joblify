package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Otp;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends DataTablesRepository<Otp, Long> {
    Optional<Otp> findByCode(String otp);

    void deleteByExpiredDateLessThan(LocalDateTime now);
}
