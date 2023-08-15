package com.ace_inspiration.team_joblify.controller.jasper_report;
import java.io.FileNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ace_inspiration.team_joblify.entity.AllCandidatesReport;
import com.ace_inspiration.team_joblify.entity.InterViewProcessReport;
import com.ace_inspiration.team_joblify.repository.AllCandidatesReportRepository;
import com.ace_inspiration.team_joblify.repository.InterviewProcessReportRepository;
import com.ace_inspiration.team_joblify.service.report_service.AllCandidatesReportService;
import com.ace_inspiration.team_joblify.service.report_service.InterviewProcessReportService;

import net.sf.jasperreports.engine.JRException;
@RestController
public class JasperReportController {
	@Autowired
	AllCandidatesReportService candidatesReportService;
	@Autowired
	AllCandidatesReportRepository candidatesReportRepo;
	@PostMapping("/view_allCandidates/{format}")
	public String generateReportCandidates(@PathVariable String format,@RequestBody List<AllCandidatesReport> tableData) throws FileNotFoundException, JRException {
		candidatesReportRepo.saveAll(tableData);
		return candidatesReportService.exportReport(format);

}
	@Autowired
	InterviewProcessReportService interViewService;
	@Autowired
	InterviewProcessReportRepository interViewRepo;
	@PostMapping("/interview_process/{format}")
	public String generateReportInterview(@PathVariable String format,@RequestBody List<InterViewProcessReport> tableData) throws FileNotFoundException, JRException {
		
		return interViewService.exportReport(format);
	
	
	
}
}
