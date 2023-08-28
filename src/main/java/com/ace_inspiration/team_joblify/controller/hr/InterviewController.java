package com.ace_inspiration.team_joblify.controller.hr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class InterviewController {
	
	
	
	@GetMapping("/interview-process")
	public String interviewProcessSummary() {
		
		return "interview-proces-summary";
	}
	
	@GetMapping("/candidate-view-summary")
	public String all_candidtate_view_summary() {
		
		return "all-candidate-view-summary";
	}
	
	
}
