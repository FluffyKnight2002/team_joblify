package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.Gender;
import com.ace_inspiration.team_joblify.entity.LanguageSkills;
import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.entity.Status;
import com.ace_inspiration.team_joblify.entity.Summary;
import com.ace_inspiration.team_joblify.entity.TechSkills;
import com.ace_inspiration.team_joblify.entity.VacancyInfo;
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
import org.springframework.web.multipart.MultipartFile;

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
    public Candidate saveCandidate(CandidateDto candidateDto) {
        List<LanguageSkills> languageSkillsList= new ArrayList<>();
        for(String languageSkill: candidateDto.getLanguageSkills()) {
            LanguageSkills  languageSkills= new LanguageSkills();
            languageSkills.setName(languageSkill);
            languageSkillsList.add(languageSkillsRepository.save(languageSkills));
        }

        List<TechSkills> techSkillsList= new ArrayList<>();
        for(String techSkill: candidateDto.getTechSkills()) {
            TechSkills  techSkills= new TechSkills();
            techSkills.setName(techSkill);
            techSkillsList.add(techSkillsRepository.save(techSkills));
            
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

       
        if (isWordFile(candidateDto.getResume()) || isPdfFile(candidateDto.getResume())) {
           candidate.setSummary(summary);
             candidate.setSelectionStatus(Status.RECEIVED);
             candidate.setInterviewStatus(Status.NONE);
             candidate.setApplyDate(LocalDateTime.now());
             candidate.setVacancyInfo(VacancyInfo.builder().id(candidateDto.getId()).build());
            candidate.setType(candidateDto.getResume().getContentType());
            try {
                candidate.setResume(Base64.getEncoder().encodeToString(candidateDto.getResume().getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        return candidateRepository.save(candidate);

    }
    private boolean isWordFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("application/msword")
                || contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
    }
    
    private boolean isPdfFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.equals("application/pdf");
    }
    


}
