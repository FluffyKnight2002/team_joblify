package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ace_inspiration.team_joblify.dto.EmailTemplateDto;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.OfferMailSended;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.entity.VacancyInfo;
import com.ace_inspiration.team_joblify.repository.OfferMailSendedRepository;
import com.ace_inspiration.team_joblify.service.OfferMailSendedService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OfferMailSendedServiceImplement implements OfferMailSendedService{
	private final OfferMailSendedRepository offerMailSendedRepository;
	
	@Override
	public void setDataInOfferMail(EmailTemplateDto emailDto) {
		Candidate candidate=new Candidate();
		candidate.setId(emailDto.getCanId());
		User user=new User();
		user.setId(emailDto.getUserId());
		VacancyInfo vacancy=new VacancyInfo();
		vacancy.setId(emailDto.getVacancyId());
		OfferMailSended offerMail=new OfferMailSended();
		offerMail.setCandidate(candidate);
		offerMail.setSendDate(LocalDateTime.now());
		offerMail.setUser(user);
		offerMail.setVacanyInfo(vacancy);
		offerMail.setNote(emailDto.getNote());
		offerMailSendedRepository.save(offerMail);
	}
}
