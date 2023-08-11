package com.ace_inspiration.team_joblify.service;


public interface EmailService {
    void sendForgetPasswordEmail(String to, String name, String otp);

    void sendJobOfferEmail(String to, String content);
}
