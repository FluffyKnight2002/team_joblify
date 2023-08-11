package com.ace_inspiration.team_joblify.service;


public interface EmailService {
    void sendForgetPasswordEmail(String to);

    void sendJobOfferEmail(String to, String content);
}
