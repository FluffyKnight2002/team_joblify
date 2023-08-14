package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.entity.User;

public interface OtpService {
    boolean otpCheck(String otp, String email);

    User emailCheck(String email);


}
