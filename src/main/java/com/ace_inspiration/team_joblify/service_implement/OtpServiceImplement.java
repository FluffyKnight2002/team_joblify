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
    public boolean otpCheck(String otp, long userId) {
        Otp o=otpRepository.findByCodeAndId(otp, userId);
        return o != null;
    }

    @Override
    public User emailCheck(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void saveOtp(String otp, long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = now.plusMinutes(3);
        Otp o=Otp.builder()
                .code(otp)
                .expiredDate(time)
                .user(User.builder().id(userId).build())
                .build();
        otpRepository.save(o);
    }
}
