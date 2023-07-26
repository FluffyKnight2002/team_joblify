package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;
import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.Gender;
import com.ace_inspiration.team_joblify.entity.LanguageSkills;
import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.entity.Status;
import com.ace_inspiration.team_joblify.entity.Summary;
import com.ace_inspiration.team_joblify.entity.TechSkills;
import com.ace_inspiration.team_joblify.repository.CandidateRepository;
import com.ace_inspiration.team_joblify.repository.SummaryRepository;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateServiceImplement implements CandidateService {
	private final CandidateRepository candidateRepository;
	private final SummaryRepository summaryRepository;
	@Override
	public List<Candidate> getAllCandidates() {
		return candidateRepository.findAll();
	}

	@Override
	
	public void saveCandidate(CandidateDto candidateDto) {
		Candidate candidate=new Candidate();
		Summary summary = new Summary();
		List<LanguageSkills> languageSkills = new ArrayList<>();
		languageSkills.add(LanguageSkills.builder().id(1).build());
		List<TechSkills> techSkills = new ArrayList<>();
		techSkills.add(TechSkills.builder().id(1).build());
		summary.setName(candidateDto.getName());
		summary.setDob(candidateDto.getDob());
		summary.setPhone(candidateDto.getPhone());
		summary.setEmail(candidateDto.getEmail());
		summary.setEducation(candidateDto.getEducation());
		Level level = Level.valueOf(candidateDto.getLvl());
		summary.setLvl(level);
		summary.setGender(Gender.valueOf(candidateDto.getGender()));
		summary.setApplyPosition(candidateDto.getApplyPosition());
		summary.setSpecialistTech(candidateDto.getSpecialistTech());
		summary.setExperience(candidateDto.getExperience());
		summary.setExpectedSalary(candidateDto.getExpectedSalary());
		summary.setSpecialistTech("Apalar skills");
		summary.setTechSkills(techSkills);
		summary.setLanguageSkills(languageSkills);
		candidate.setSummary(summaryRepository.save(summary));
		candidate.setSelectionStatus(Status.RECEIVED);
		candidate.setInterviewStatus(Status.NONE);
		candidate.setApplyDate(LocalDateTime.now());
		try {
			candidate.setResume(encodeImageToString(candidateDto.getResume()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		candidateRepository.save(candidate);	
	}
	

	
	

//	public SummaryDto getSummaryFromCandidate(long candidateId) {
//        Candidate candidate = // Fetch the Candidate entity using candidateId
//        if (candidate == null) {
//            throw new EntityNotFoundException("Candidate not found with id: " + candidateId);
//        }
//
//        SummaryDto summaryDto = SummaryDtoConverter.mapCandidateToSummaryDto(candidate);
//        return summaryDto;
//    }

//	@Override
//	public SummaryDto getSummaryFromCandidate() {
//		SummaryDto summaryDTO = new SummaryDto();
//		CandidateDto candidate = new Candidate();
//		summaryDTO.setId(candidate.getSummary().getId());
//		summaryDTO.setName(candidate.getSummary().getName());
//		summaryDTO.setDob(candidate.getSummary().getDob());
//		summaryDTO.setGender(candidate.getSummary().getGender().toString());
//		summaryDTO.setPhone(candidate.getSummary().getPhone());
//		summaryDTO.setEmail(candidate.getSummary().getEmail());
//		summaryDTO.setEducation(candidate.getSummary().getEducation());
//		summaryDTO.setApplyPosition(candidate.getSummary().getApplyPosition());
//		summaryDTO.setLvl(candidate.getSummary().getLvl().toString());
//		summaryDTO.setSpecialistTech(candidate.getSummary().getSpecialistTech());
//		summaryDTO.setExperience(candidate.getSummary().getExperience());
//		summaryDTO.setExpectedSalary(candidate.getSummary().getExpectedSalary());
//
//		List<String> languageSkills = new ArrayList<>();
//		for (LanguageSkills languageSkill : candidate.getSummary().getLanguageSkills()) {
//			languageSkills.add(languageSkill.getName());
//		}
//		summaryDTO.setLanguageSkills(languageSkills);
//
//		List<String> techSkills = new ArrayList<>();
//		for (TechSkills techSkill : candidate.getSummary().getTechSkills()) {
//			techSkills.add(techSkill.getSkillName());
//		}
//		summaryDTO.setTechSkills(techSkills);
//
//		return summaryDTO;
//	}

	public String encodeImageToString(MultipartFile file) throws IOException {
		byte[] bytes = file.getBytes();
		return Base64Utils.encodeToString(bytes);
	}
}
