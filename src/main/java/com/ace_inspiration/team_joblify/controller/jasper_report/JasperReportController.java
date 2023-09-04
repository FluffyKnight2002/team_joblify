package com.ace_inspiration.team_joblify.controller.jasper_report;

import static org.mockito.Answers.valueOf;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.service.ReportService;

import net.sf.jasperreports.engine.JRException;

@Controller
public class JasperReportController {

	@Autowired
	private ReportService reportService;

	@GetMapping("/all_candidates/{format}")
	public ResponseEntity<byte[]> generateReportCandidates(@PathVariable String format,
			@RequestParam(name = "filter", required = false) int filter,
			@RequestParam(name = "openDate", required = false) String openDate,
			@RequestParam(name = "position", required = false) String position,
			@RequestParam(name = "level", required = false) String level,
			@RequestParam(name = "selectionStatus", required = false) String selectionStatus,
			@RequestParam(name = "interviewStatus", required = false) String interviewStatus)
			throws JRException, IOException {

		LocalDate startDate = null;
		LocalDate endDate = null;
		List<Level> levelList = new ArrayList<>();

		if (filter == 1) {

			if (!level.isEmpty()) {
				String[] levelValues = level.split(", ");
				for (String levelValue : levelValues) {

					Level levels = Level.valueOf(levelValue);
					levelList.add(levels);

				}
			}

				System.out.println(openDate + "acwsca");


			if (openDate != null) {
				LocalDate currentDate = LocalDate.now();

				if (openDate.equals("Today")) {
					startDate = currentDate;
					endDate = currentDate;
				} else if (openDate.equals("Last Week")) {
					startDate = currentDate.minusDays(7);
					endDate = currentDate;
				} else if (openDate.equals("Last Month")) {
					startDate = currentDate.minusMonths(1);
					endDate = currentDate;
				} else if (openDate.equals("Last 6 Months")) {
					startDate = currentDate.minusMonths(6);
					endDate = currentDate;
				} else if (openDate.equals("Last Year")) {
					startDate = currentDate.minusYears(1);
					endDate = currentDate;
				} else {
					String[] parts = openDate.split(" - ");

					String startDateStr = parts[0];
					String endDateStr = parts[1];

					// Replace '/' with '-' in both date parts
					startDateStr = startDateStr.replace('/', '-');
					endDateStr = endDateStr.replace('/', '-');

					// Parse the modified date strings into LocalDate objects
					startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
					endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
				}
			}
		}
		System.out.println("start=" + startDate);
		System.out.println("end=" + endDate);
		return reportService.allCandidate(format, startDate, endDate, position, levelList, selectionStatus,
				interviewStatus);

	}

	@GetMapping("/interview_process/{format}")
	public ResponseEntity<byte[]> generateReportInterview(@PathVariable String format,
			@RequestParam(name = "filter", required = false) int filter,
			@RequestParam(name = "openDate", required = false) String openDate,
			@RequestParam(name = "position", required = false) String position,
			@RequestParam(name = "department", required = false) String department) throws JRException, IOException {
		LocalDate startDate = null;
		LocalDate endDate = null;

		System.out.println(format + filter + openDate + position + department);

		if (filter == 1 && openDate != null) {
			LocalDate currentDate = LocalDate.now();

			if (openDate.equals("Today")) {
				startDate = currentDate;
				endDate = currentDate;
			} else if (openDate.equals("Last Week")) {
				startDate = currentDate.minusDays(7);
				endDate = currentDate;
			} else if (openDate.equals("Last Motnth")) {
				startDate = currentDate.minusMonths(1);
				endDate = currentDate;
			} else if (openDate.equals("Last 6 Month")) {
				startDate = currentDate.minusMonths(6);
				endDate = currentDate;
			} else if (openDate.equals("Last Year")) {
				startDate = currentDate.minusYears(1);
				endDate = currentDate;
			}

			else {
				String[] parts = openDate.split("-");

				if (parts.length == 2) {
					String startDateStr = parts[0];
					String endDateStr = parts[1];

					// Replace '/' with '-' in both date parts
					startDateStr = startDateStr.replace('/', '-');
					endDateStr = endDateStr.replace('/', '-');

					// Parse the modified date strings into LocalDate objects
					startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
					endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
				}
			}
		}

		System.out.println("start=" + startDate);
		System.out.println("end=" + endDate);
		return reportService.interviewProcess(format, startDate, endDate, department, position);
	}

}
