package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import java.util.List;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;
import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.dto.SummaryDto;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.Gender;
import com.ace_inspiration.team_joblify.entity.Interview;
import com.ace_inspiration.team_joblify.entity.InterviewType;
import com.ace_inspiration.team_joblify.entity.LanguageSkills;
import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.entity.Status;
import com.ace_inspiration.team_joblify.entity.Summary;
import com.ace_inspiration.team_joblify.entity.TechSkills;
import com.ace_inspiration.team_joblify.repository.CandidateRepository;
import com.ace_inspiration.team_joblify.repository.InterviewRepository;
import com.ace_inspiration.team_joblify.repository.LanguageSkillsRepository;
import com.ace_inspiration.team_joblify.repository.SummaryRepository;
import com.ace_inspiration.team_joblify.repository.TechSkillsRepository;
import com.ace_inspiration.team_joblify.service.InterviewService;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;

import lombok.RequiredArgsConstructor;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.entity.Status;
import com.ace_inspiration.team_joblify.repository.CandidateRepository;
import com.ace_inspiration.team_joblify.repository.PositionRepository;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class CandidateServiceImplement implements CandidateService{
	
    private final CandidateRepository candidateRepository;
    private final SummaryRepository summaryRepository;
    private final InterviewService interviewService;
    private final LanguageSkillsRepository languageSkillsRepository;
    private final TechSkillsRepository techSkillsRepository;




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
		         List<Interview> interviews = interviewService.findInterviewsByCandidateId(id);
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

    public void saveCandidate(CandidateDto candidateDto) {
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

       
        if (isWordFile(candidateDto.getResume()) || isPdfFile(candidateDto.getResume()) || isPngFile(candidateDto.getResume()) || isJpgFile(candidateDto.getResume())) {
        	 candidate.setSummary(summary);
             candidate.setSelectionStatus(Status.RECEIVED);
             candidate.setInterviewStatus(Status.NONE);
             candidate.setApplyDate(LocalDateTime.now());
            candidate.setType(candidateDto.getResume().getContentType());
            try {
                candidate.setResume(Base64.getEncoder().encodeToString(candidateDto.getResume().getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            candidateRepository.save(candidate);

        }  

       


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

    public String encodeImageToString(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        return Base64Utils.encodeToString(bytes);
    }
    
    @Override
    public List<Candidate> getFile(List<Long> id) {
    	  return candidateRepository.findByIdIn(id);
      }
}
