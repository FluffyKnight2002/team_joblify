package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.entity.User;

public interface OtpService {
    boolean otpCheck(String otp, long userId);

    User emailCheck(String email);

    void saveOtp(String otp, long userId);
}
