package com.ace_inspiration.team_joblify.controller.hr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class InterviewController {
	
	
	
	@GetMapping("/interviewProcess")
	public String interviewProcessSummary() {
		
		return "interview-proces-summary";
	}
	
	@GetMapping("/CandidateViewSummary")
	public String all_candidtate_view_summary() {
		
		return "all-candidate-view-summary";
	}
	
	@GetMapping("/viewWithPosition")
	public String viewWithPosition() {
		return "all-candidate-view-summary";
	}
	
	@GetMapping("/interviewCandidat")
	public String interviewCandidat() {
		return "all-candidate-view-summary";
	}
	@GetMapping("/passedCandidat")
	public String passedCandidat() {
		return "all-candidate-view-summary";
	}
	@GetMapping("/pendingCandidat")
	public String pendingCandidat() {
		return "all-candidate-view-summary";
	}
	@GetMapping("/cancelCandidat")
	public String cancelCandidat() {
		return "all-candidate-view-summary";
	}
	@GetMapping("/notInterviewCandidat")
	public String notInterviewCandidat() {
		return "all-candidate-view-summary";
	}
	@GetMapping("/offerMailLetter")
	public String offerMailLetter() {
		return "all-candidate-view-summary";
	}
	@GetMapping("/acceptedCandidat")
	public String acceptedCandidat() {
		return "all-candidate-view-summary";
	}

	@GetMapping("/sendEmail")
	public String send_email() {
		return "send-email";
	}
}
