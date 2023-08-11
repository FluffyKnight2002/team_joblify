package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.repository.OtpRepository;
import com.ace_inspiration.team_joblify.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImplement implements ScheduleService {

    private final OtpRepository otpRepository;

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void otpClear() {
        LocalDateTime now = LocalDateTime.now();
        otpRepository.deleteByExpiredDateLessThan(now);
    }
}
