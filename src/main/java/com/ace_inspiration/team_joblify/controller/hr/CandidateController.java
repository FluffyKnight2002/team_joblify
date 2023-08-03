package com.ace_inspiration.team_joblify.controller.hr;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.entity.AllPost;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.InterviewProcess;
import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.AllPostRepository;
import com.ace_inspiration.team_joblify.repository.CandidateRepository;
import com.ace_inspiration.team_joblify.repository.InterviewProcessRepository;
import com.ace_inspiration.team_joblify.service_implement.PositionServiceImpl;
import com.ace_inspiration.team_joblify.service_implement.candidate_service_implement.CandidateServiceImplement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CandidateController {
	
	private final CandidateServiceImplement candidateService;
	
	private final PositionServiceImpl positioinService;
	
	private final InterviewProcessRepository interview;
	
	private final AllPostRepository allPost;
	
	private final CandidateRepository repo;
	
	@GetMapping("/allCandidate")
	@ResponseBody
	public DataTablesOutput<InterviewProcess> getAllCandidate(DataTablesInput input) {
   DataTablesOutput<InterviewProcess> interviewData= interview.findAll(input);
	    return interviewData;
	}

	
	@GetMapping("/allPositions")
	@ResponseBody
	public ResponseEntity<List<Position>> getAllPosition(){
		List<Position> position=positioinService.selectAllPosition();
		
		return ResponseEntity.ok(position);
	}
	@PostMapping("/changStatus")
	public ResponseEntity<?> changeStatus(@RequestBody long id){
		 candidateService.changeStatus(id);
		 return ResponseEntity.ok("OK Change Status");
	}
	
	@PostMapping("/seeMore")
	@ResponseBody
	public ResponseEntity<?> updateStatus(@RequestBody long id) {
	    Optional<Candidate> candiDate=candidateService.findByid(id);
	    if (candiDate.isPresent()) {
	        Candidate candidate = candiDate.get();
	        CandidateDto candidateDto = new CandidateDto(
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
	        System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+candidateDto.getEmail());
	   
	    return ResponseEntity.ok(candidateDto);
	
	}else {
		 return ResponseEntity.ok("error");
	}
	}
	
	@PostMapping("/changeInterview")
	@ResponseBody
	public ResponseEntity<?> changeInterview(@RequestParam("id") long id,@RequestParam("status") String status) {
		
		candidateService.changeInterviewstatus(id, status);
		return ResponseEntity.ok("okokok");
	}

	@GetMapping("/process")
	@ResponseBody
	public DataTablesOutput<AllPost> interviewProcess(DataTablesInput input){
		DataTablesOutput<AllPost> allpost=allPost.findAll(input);
		return allpost;
	}
}
