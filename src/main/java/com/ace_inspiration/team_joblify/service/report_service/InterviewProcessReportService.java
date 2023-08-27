package com.ace_inspiration.team_joblify.service.report_service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.ace_inspiration.team_joblify.entity.AllPost;
import com.ace_inspiration.team_joblify.entity.InterViewProcessReport;
import com.ace_inspiration.team_joblify.repository.AllPostRepository;
import com.ace_inspiration.team_joblify.repository.InterviewProcessReportRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
	@Service
	public class InterviewProcessReportService {

	    @Autowired
	    private InterviewProcessReportRepository interRepository;

	    public String exportReport(String reportFormat) throws FileNotFoundException, JRException {
	        String basePath = "C:\\Users\\HP PC\\Desktop\\Project"; // Update this with your desired base path
	        List<InterViewProcessReport> interview_process = interRepository.findAll();
	        System.out.print(interview_process);

	        // Load file and compile it
	        File file = ResourceUtils.getFile("classpath:all_candidates.jrxml");
	        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
	        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(interview_process );
	        Map<String, Object> parameters = new HashMap<>();
	        parameters.put("createdBy", "Interview Process");
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
	         if (reportFormat.equalsIgnoreCase("pdf")) {
	            String outputPath = basePath + "\\interview_process.pdf";
	            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
	        } else if (reportFormat.equalsIgnoreCase("excel")) {
	            String outputPath = basePath + "\\interview_process.xlsx";
	            JRXlsxExporter exporter = new JRXlsxExporter();
	            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputPath));
	            exporter.exportReport();
	        } else {
	            return "Invalid report format specified.";
	        }

	        return "Report generated in path: " + basePath;
	    }
	}
