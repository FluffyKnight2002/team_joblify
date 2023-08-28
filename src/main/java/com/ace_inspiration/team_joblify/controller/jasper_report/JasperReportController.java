package com.ace_inspiration.team_joblify.controller.jasper_report;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ace_inspiration.team_joblify.entity.AllCandidatesReport;
import com.ace_inspiration.team_joblify.repository.AllCandidatesReportRepository;
import com.ace_inspiration.team_joblify.service.ReportService;
import com.ace_inspiration.team_joblify.service_implement.ReportServiceImplement;

import net.sf.jasperreports.engine.JRException;

@Controller
public class JasperReportController {

	@Autowired
	private ReportService reportService;

	@GetMapping("/all_candidates/{format}")
	public ResponseEntity<byte[]> generateReportCandidates(@PathVariable String format) throws JRException, IOException {
		
		return reportService.allCandidate(format);

	}

	@GetMapping("/interview_process/{format}")
	public ResponseEntity<byte[]> generateReportInterview(@PathVariable String format) throws JRException, IOException {

		return reportService.interviewProcess(format);

	}
}
