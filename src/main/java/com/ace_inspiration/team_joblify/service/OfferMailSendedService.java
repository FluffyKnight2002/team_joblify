package com.ace_inspiration.team_joblify.service;

import com.ace_inspiration.team_joblify.dto.EmailTemplateDto;

public interface OfferMailSendedService {
	
	void setDataInOfferMail(EmailTemplateDto emaildto);

}
