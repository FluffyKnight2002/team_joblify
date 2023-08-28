package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.dto.SummaryDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.*;
import com.ace_inspiration.team_joblify.service.*;
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
    private final InterviewService interviewService;
    private final LanguageSkillsRepository languageSkillsRepository;
    private final TechSkillsRepository techSkillsRepository;
    private final VacancyInfoRepository vacancyInfoRepository;




    private final EntityManager entityManager;

	@Override
	public DataTablesOutput<Candidate> getAllcandidate(DataTablesInput input){
		return candidateRepository.findAll(input);
	}


	@Override
	public SummaryDto findByid(long id) {
		Optional<Candidate> candiDate=candidateRepository.findById(id);

		   if (candiDate.isPresent()) {
		         Candidate candidate = candiDate.get();
		         String interviewType;
		         List<String>interviewStages=new ArrayList<>();
//		         List<String> lan=new ArrayList<>(); 
//		         List<String> tec=new ArrayList<>();
		         List<Interview> interviews = interviewService.findInterviewsByCandidateId(id);
//                 List<LanguageSkills> languageSkill=languageSkillsRepository.findLanguageSkillIdBySummaryId(candidate.getSummary().getId());
//		        List<TechSkills> techSkill=techSkillsRepository.findTechSkillsIdBySummaryId(candidate.getSummary().getId());
//		        if(!languageSkill.isEmpty()) {
//		        	languageSkill=languageSkill.get(0).getSummary().get(0).getLanguageSkills();
//		        	for(LanguageSkills language: languageSkill) {
//		        		lan.add(language.getName());
//		        		System.err.println(">>>>>>>>>>>>>>>>"+language.getName());
//		        	}
//		        }else {
//		        	lan.add("not have data");
//		        }
//		        if(!techSkill.isEmpty()) {
//		        	techSkill=techSkill.get(0).getSummary().get(0).getTechSkills();
//		        	for(TechSkills tech: techSkill) {
//		        		tec.add(tech.getName());
//		        		System.err.println(">>>>>>>>>>>>>>>>"+tech.getName());
//		        	}
//		        }else {
//		        	lan.add("not have data");
//		        }
                 if(!interviews.isEmpty()) {
		        	 interviewType=interviews.get(0).getType().toString();
		        	 for (Interview interview : interviews) {
		        		 interviewStages.add(interview.getInterviewStage().toString());
		        		 
		        	 }
		         }else {
		        	 interviewStages.add("Not in avable");
		        	 interviewType="Not in Interview";
		         }
		         
		         SummaryDto summaryDto = new SummaryDto(
		             candidate.getId(),
		             candidate.getSummary().getName(),
		             candidate.getSummary().getEmail(),
		             candidate.getSelectionStatus(),
		             candidate.getInterviewStatus(),
		             candidate.getSummary().getDob(),
		             candidate.getSummary().getApplyPosition(),
		             candidate.getSummary().getEducation(),
		             candidate.getSummary().getExperience(),
		             candidate.getSummary().getExpectedSalary(),
		             candidate.getSummary().getGender(),
		             candidate.getSummary().getLvl(),
		             candidate.getSummary().getPhone(),
		             candidate.getSummary().getSpecialistTech(),
		         	candidate.getVacancyInfo().getVacancy().getPosition().getName(),
		         	interviewStages,
		         	interviewType
//                         lan,
//                         tec
		         );
		       

		     return summaryDto;

		 }else {
		 	 return null;
		 }

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
	public void stage(long id) {
		 Candidate candidate = entityManager.find(Candidate.class, id);
	        if (candidate != null) {
	            candidate.setSelectionStatus(Status.CONSIDERING); // Set the new status value
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
            System.out.println("Language Skills : " + languageSkill);
            if (languageSkill.trim() != "") {
                Optional<LanguageSkills>  optionalLanguageSkills= languageSkillsRepository.findByNameIgnoreCase(languageSkill);
                if(!optionalLanguageSkills.isPresent()) {
                    LanguageSkills languageSkillsToSave = LanguageSkills.builder().name(languageSkill).build();
                    languageSkillsList.add(languageSkillsRepository.save(languageSkillsToSave));
                }else {
                    languageSkillsList.add(optionalLanguageSkills.get());
                }
            }
        }

        List<TechSkills> techSkillsList= new ArrayList<>();
        for(String techSkill: candidateDto.getTechSkills()) {
            System.out.println("Tech Skills : " + techSkill);
            if (techSkill.trim() != "") {
                Optional<TechSkills>  optionalTechSkills = techSkillsRepository.findByNameIgnoreCase(techSkill);
                if(!optionalTechSkills.isPresent()) {
                    TechSkills techSkillsToSave = TechSkills.builder().name(techSkill).build();
                    techSkillsList.add(techSkillsRepository.save(techSkillsToSave));
                }else {
                    techSkillsList.add(optionalTechSkills.get());
                }
            }
        }

        Optional<VacancyInfo> vacancyInfo = vacancyInfoRepository.findById(candidateDto.getId());


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


        if (isWordFile(candidateDto.getResume()) || isPdfFile(candidateDto.getResume()) || isPngFile(candidateDto.getResume()) || isJpgFile(candidateDto.getResume())) {
        	 candidate.setSummary(summary);
             candidate.setSelectionStatus(Status.RECEIVED);
             candidate.setInterviewStatus(Status.NONE);
             candidate.setApplyDate(LocalDateTime.now());
//             candidate.setVacancyInfo(van.getId());
             candidate.setVacancyInfo(vacancyInfo.get());
            candidate.setType(candidateDto.getResume().getContentType());
            try {
                candidate.setResume(Base64.getEncoder().encodeToString(candidateDto.getResume().getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
           candidateRepository.save(candidate);


        }
		return candidate;



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
    private boolean isWordFile(MultipartFile file) {
        return file.getContentType().equals("application/msword")
                || file.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    }

    private boolean isPdfFile(MultipartFile file) {
        return file.getContentType().equals("application/pdf");
    }
    private boolean isPngFile(MultipartFile file) {
    	return file.getContentType().equals("image/png");
    }
    private boolean isJpgFile(MultipartFile file) {
    	return file.getContentType().equals("image/jpg");
    }


    
    @Override
    public List<Candidate> getFile(List<Long> id) {
    	  return candidateRepository.findByIdIn(id);
      }
}
