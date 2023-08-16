package com.ace_inspiration.team_joblify.controller.hr;




import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.dto.CountDto;
import com.ace_inspiration.team_joblify.dto.SummaryDto;
import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.repository.CandidateRepository;
import com.ace_inspiration.team_joblify.repository.DasboardRespository;
import com.ace_inspiration.team_joblify.entity.AllPost;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.InterviewProcess;
import com.ace_inspiration.team_joblify.repository.AllPostRepository;
import com.ace_inspiration.team_joblify.repository.InterviewProcessRepository;
import com.ace_inspiration.team_joblify.repository.VacancyInfoRepository;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;
import com.ace_inspiration.team_joblify.service_implement.PositionServiceImpl;
import com.ace_inspiration.team_joblify.service_implement.candidate_service_implement.CandidateServiceImplement;
import com.ace_inspiration.team_joblify.service_implement.candidate_service_implement.SummaryServiceImplement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CandidateController {

	private final CandidateService candidateService;

	private final CandidateServiceImplement candidateImpl;

	private final SummaryServiceImplement summaryServiceImplement;

	private final PositionServiceImpl positioinService;
	
	private final DasboardRespository dasboard;

	private final InterviewProcessRepository interview;
	
	private final AllPostRepository allPost;
	
	private final VacancyInfoRepository van;
	
	private final CandidateRepository repo;
	
	@GetMapping("/allCandidate")
	@ResponseBody
	public DataTablesOutput<InterviewProcess> getAllCandidate(@Valid DataTablesInput input,ModelMap map) {
   		DataTablesOutput<InterviewProcess> interviewData= interview.findAll(input);
		return interviewData;
	}

	
	@GetMapping("/allPositions")
	@ResponseBody
	public List<Position> getAllPosition(){
		List<Position> position=positioinService.selectAllPosition();
		return position;
	}

	@PostMapping("/changStatus")
	public void changeStatus(@RequestBody long id){
		 candidateImpl.changeStatus(id);
	}
	
	 @PostMapping("/seeMore")
	 @ResponseBody
	 public SummaryDto updateStatus(@RequestBody long id) {
	     Optional<Candidate> candiDate=candidateService.findByid(id);
	     if (candiDate.isPresent()) {
	         Candidate candidate = candiDate.get();
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
	         	candidate.getVacancyInfo().getVacancy().getPosition().getName()
	         );
	         System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+summaryDto.getEmail());

	     return summaryDto;

	 }else {
	 	 return null;
	 }
	 }
	
	@PostMapping("/changeInterview")
	@ResponseBody
	public ResponseEntity<?> changeInterview(@RequestParam("id") long id,@RequestParam("status") String status) {
		
		candidateImpl.changeInterviewstatus(id, status);
		return ResponseEntity.ok("okokok");
	}

	@GetMapping("/process")
	@ResponseBody
	public DataTablesOutput<AllPost> interviewProcess(DataTablesInput input){
		DataTablesOutput<AllPost> allpost=allPost.findAll(input);
		return allpost;
	}
	
	@GetMapping("/count")
	@ResponseBody
	public List<CountDto> getCounts() {
	    List<Object[]> results = van.getVacancyInfoWithCandidateCounts();
	    
	    List<CountDto> dtos = new ArrayList<>();
	    for (Object[] result : results) {
	        CountDto dto = new CountDto();
	        dto.setId((long) result[0]);
	        
	        // Convert java.sql.Date to LocalDate
	        dto.setClose(( (Date) result[1]).toLocalDate()); 
	        dto.setOpen(( (Date) result[2]).toLocalDate());
	        
	        dto.setPostTotal((int) result[3]);
	        dto.setHired((int) result[4]);
	        dto.setTotalCandidates( (long) result[5]); // Make sure this is appropriate
	        
	        dtos.add(dto);
	        System.err.println(result[1].toString() + result[2].toString());
	    }
	    return dtos;
	}
	

	
	
	@ModelAttribute("candidate")
	public CandidateDto getCandidateDto() {
		return new CandidateDto();
	}


	@PostMapping("/apply-job")
	public String submitJobDetail(@ModelAttribute("candidate") CandidateDto dto) {
		System.out.println(dto);
		candidateService.saveCandidate(dto);
		return "redirect:/show-job-details";

	}

}
