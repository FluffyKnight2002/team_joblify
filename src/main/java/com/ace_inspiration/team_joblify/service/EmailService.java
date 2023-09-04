package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.EmailTemplateDto;

public interface EmailService {
    void sendForgetPasswordEmail(String to);
    boolean sendJobOfferEmail(EmailTemplateDto emailTemplateDto);
    boolean sendDirectMail(String name,String email,String about,String message);
}
