package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
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
import com.ace_inspiration.team_joblify.repository.LanguageSkillsRepository;
import com.ace_inspiration.team_joblify.repository.SummaryRepository;
import com.ace_inspiration.team_joblify.repository.TechSkillsRepository;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;

import lombok.RequiredArgsConstructor;



import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class CandidateServiceImplement implements CandidateService{
    private final CandidateRepository candidateRepository;
    private final SummaryRepository summaryRepository;
    private final LanguageSkillsRepository languageSkillsRepository;
    private final TechSkillsRepository techSkillsRepository;

    


	@Autowired
    private EntityManager entityManager;

	@Override
	public DataTablesOutput<Candidate> getAllcandidate(DataTablesInput input){
		return candidateRepository.findAll(input);
	}


	@Override
	public Optional<Candidate> findByid(long id) {
		return candidateRepository.findById(id);

	}

	@Override
	@Transactional
    public void changeStatus(long id) {
      Candidate candidate = entityManager.find(Candidate.class, id);
	        if (candidate != null) {
	            candidate.setSelectionStatus(Status.VIEWED); // Set the new status value
	            entityManager.persist(candidate); // Save the updated candidate entity
	        }
	    }

	@Override
	@Transactional
    public void changeInterviewstatus(long id,String status) {
      Candidate candidate = entityManager.find(Candidate.class, id);
	        if (candidate != null) {
	            candidate.setInterviewStatus(Status.valueOf(status)); // Set the new status value
	            entityManager.persist(candidate); // Save the updated candidate entity
	        }
	    }
    @Override
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override

    public void saveCandidate(CandidateDto candidateDto) {
//        List<LanguageSkills> languageSkillsList= new ArrayList<>();
//        for(String languageSkill: candidateDto.getLanguageSkills()) {
//            LanguageSkills  languageSkills= new LanguageSkills();
//            languageSkills.setName(languageSkill);
//            languageSkillsList.add(languageSkills);
//            languageSkillsRepository.save(languageSkills);
//        }
//
//        List<TechSkills> techSkillsList= new ArrayList<>();
//        for(String techSkill: candidateDto.getTechSkills()) {
//            TechSkills  techSkills= new TechSkills();
//            techSkills.setName(techSkill);
//            techSkillsList.add(techSkills);
//            techSkillsRepository.save(techSkills);
//        }



        Summary summary = new Summary();
        summary.setName(candidateDto.getName());
        summary.setDob(candidateDto.getDob());
        summary.setPhone(candidateDto.getPhone());
        summary.setEmail(candidateDto.getEmail());
        summary.setEducation(candidateDto.getEducation());
        summary.setLvl(Level.valueOf(candidateDto.getLvl()));
        summary.setGender(Gender.valueOf(candidateDto.getGender()));
        summary.setApplyPosition(candidateDto.getApplyPosition());
        summary.setSpecialistTech(candidateDto.getSpecialistTech());
        summary.setExperience(candidateDto.getExperience());
        summary.setExpectedSalary(candidateDto.getExpectedSalary());
        summary.setSpecialistTech(candidateDto.getSpecialistTech());
        summary.setLanguageSkills(candidateDto.getLanguageSkills());
        summary.setTechSkills(candidateDto.getTechSkills());
        summaryRepository.save(summary);

        Candidate candidate=new Candidate();

        candidate.setSummary(summary);
        candidate.setSelectionStatus(Status.RECEIVED);
        candidate.setInterviewStatus(Status.NONE);
        candidate.setApplyDate(LocalDateTime.now());
        try {
            candidate.setResume(Base64.getEncoder().encodeToString(candidateDto.getResume().getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        candidateRepository.save(candidate);



//		summary.getLanguageSkills().add(languageSkills);
//		summary.getTechSkills().add(techSkills);
//
//		candidateRepository.save(candidate);
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
