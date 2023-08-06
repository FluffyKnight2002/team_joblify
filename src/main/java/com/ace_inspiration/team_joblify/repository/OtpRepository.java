package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.entity.Otp;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OtpRepository extends DataTablesRepository<Otp, Long> {
    Otp findByCodeAndId(String otp, long userId);

    void deleteByExpiredDateLessThan(LocalDateTime now);
}
