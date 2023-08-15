package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.entity.Otp;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.OtpRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class OtpServiceImplement implements OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    @Override
    public boolean otpCheck(String otp, String email) {
        LocalDateTime now = LocalDateTime.now();
        Otp o=otpRepository.findByCode(otp).orElse(null);

        return o != null && o.getExpiredDate().isAfter(now);
    }

    @Override
    public User emailCheck(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }



}
