package com.ace_inspiration.team_joblify.controller.jasper_report;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ace_inspiration.team_joblify.entity.AllCandidatesReport;
import com.ace_inspiration.team_joblify.entity.AllPost;
import com.ace_inspiration.team_joblify.entity.InterViewProcessReport;
import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.repository.AllCandidatesReportRepository;
import com.ace_inspiration.team_joblify.repository.AllPostRepository;
import com.ace_inspiration.team_joblify.repository.InterviewProcessReportRepository;
import com.ace_inspiration.team_joblify.service.AllPostService;
import com.ace_inspiration.team_joblify.service.report_service.AllCandidatesReportService;
import com.ace_inspiration.team_joblify.service.report_service.InterviewProcessReportService;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
@Controller
public class JasperReportController {
	@Autowired
	AllCandidatesReportService candidatesReportService;
	@Autowired
	AllCandidatesReportRepository candidatesReportRepo;
	@Autowired
	InterviewProcessReportService interviewReportService;
	@Autowired
	InterviewProcessReportRepository interviewReportRepo;
	
	@PostMapping("/view_allCandidates/{format}")
	public String generateReportCandidates(@PathVariable String format,@RequestBody List<AllCandidatesReport> tableData) throws FileNotFoundException, JRException {
		candidatesReportRepo.saveAll(tableData);
		return candidatesReportService.exportReport(format);

}
	@GetMapping("/view_interview_process/{format}")
	public String generateReportInterview(@PathVariable String format) throws FileNotFoundException, JRException {
		System.out.print("AAwwwwwwwwwwwwwww");
		return interviewReportService.exportReport(format);
	
	}
	
	
}

