package com.ace_inspiration.team_joblify.service.report_service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.ace_inspiration.team_joblify.entity.AllCandidatesReport;
import com.ace_inspiration.team_joblify.repository.AllCandidatesReportRepository;
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
public class AllCandidatesReportService {
	@Autowired
	private AllCandidatesReportRepository jasperRepository;
	public String exportReport(String reportFormat) throws FileNotFoundException, JRException {
        List<AllCandidatesReport> pdfjasper = jasperRepository.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:all_candidates.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(pdfjasper);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Joblify Team");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint+ "\\All CandidatesReport.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint+ "\\All CandidatesReport.pdf");
        }
        if (reportFormat.equalsIgnoreCase("excel")) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("\\All CandidatesReport.xlsx"));
            exporter.exportReport();
        }
        return "report generated in path : ";
    }
}

	
	


