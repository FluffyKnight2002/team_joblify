package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.CandidateRepository;
import com.ace_inspiration.team_joblify.repository.LanguageSkillsRepository;
import com.ace_inspiration.team_joblify.repository.SummaryRepository;
import com.ace_inspiration.team_joblify.repository.TechSkillsRepository;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CandidateServiceImplement implements CandidateService{
    private final CandidateRepository candidateRepository;
    private final SummaryRepository summaryRepository;
    private final LanguageSkillsRepository languageSkillsRepository;
    private final TechSkillsRepository techSkillsRepository;




    private final EntityManager entityManager;

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
        List<LanguageSkills> languageSkillsList= new ArrayList<>();
        for(String languageSkill: candidateDto.getLanguageSkills()) {
            LanguageSkills  languageSkills= new LanguageSkills();
            languageSkills.setName(languageSkill);
            languageSkillsList.add(languageSkills);
            languageSkillsRepository.save(languageSkills);
        }

        List<TechSkills> techSkillsList= new ArrayList<>();
        for(String techSkill: candidateDto.getTechSkills()) {
            TechSkills  techSkills= new TechSkills();
            techSkills.setName(techSkill);
            techSkillsList.add(techSkills);
            techSkillsRepository.save(techSkills);
        }



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
        summary.setLanguageSkills(languageSkillsList);
        summary.setTechSkills(techSkillsList);
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
    }




}
