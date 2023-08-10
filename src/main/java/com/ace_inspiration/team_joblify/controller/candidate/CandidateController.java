package com.ace_inspiration.team_joblify.controller.candidate;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ace_inspiration.team_joblify.dto.CandidateDto;
import com.ace_inspiration.team_joblify.entity.Summary;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;
import com.ace_inspiration.team_joblify.service_implement.candidate_service_implement.SummaryServiceImplement;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CandidateController {

	private final CandidateService candidateService;
	private final SummaryServiceImplement summaryServiceImplement;

	@ModelAttribute("candidate")
	public CandidateDto getCandidateDto() {
		return new CandidateDto();
	}

	@GetMapping("/job-details")
	public String ShowJobDetail() {
		return "job-details";
	}

	@PostMapping("/apply-job")
	public String submitJobDetail(@ModelAttribute("candidate") CandidateDto dto) {
		candidateService.saveCandidate(dto);
		return "redirect:/show-job-details";

	}

	@GetMapping("/view-summaryinfo")
	public String ViewSummaryInfo(Model model) {
		List<Summary> summaries = summaryServiceImplement.getAllSummarys();
		model.addAttribute("listsummaryinfo", summaries);
		return "view-summaryinfo";

	}

}
