package com.ace_inspiration.team_joblify.service.report_service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.ace_inspiration.team_joblify.entity.AllCandidatesReport;
import com.ace_inspiration.team_joblify.entity.InterViewProcessReport;
import com.ace_inspiration.team_joblify.entity.InterviewProcess;
import com.ace_inspiration.team_joblify.repository.InterviewProcessReportRepository;
import com.ace_inspiration.team_joblify.repository.InterviewProcessRepository;

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
	private InterviewProcessReportRepository jasperRepository;
	
	public ResponseEntity<byte[]> exportReport(String reportFormat) throws JRException, IOException {
	    List<InterViewProcessReport> pdfjasper = jasperRepository.findAll();
	    System.out.println(pdfjasper);
	    // Load file and compile it
	    InputStream jrxmlStream = getClass().getResourceAsStream("/interview_process.jrxml");
	    JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

	    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(pdfjasper);
	    Map<String, Object> parameters = new HashMap<>();
	    parameters.put("createdBy", "Interview Process");
	    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

	    byte[] reportBytes;
	    String fileName;
	    String contentType;

	    if (reportFormat.equalsIgnoreCase("pdf")) {
	        reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);
	        fileName = "Cost_Report.pdf";
	        contentType = MediaType.APPLICATION_PDF_VALUE;
	    } else if (reportFormat.equalsIgnoreCase("excel")) {
	        JRXlsxExporter exporter = new JRXlsxExporter();
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
	        exporter.exportReport();
	        reportBytes = byteArrayOutputStream.toByteArray();
	        fileName = "Cost_Report.xlsx";
	        contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
	    } else {
	        return ResponseEntity.badRequest().build(); // Invalid report format
	    }

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentDispositionFormData(fileName, fileName);
	    headers.setContentType(MediaType.parseMediaType(contentType));

	    return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
	}


    private byte[] getFileBytes(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }
}

	

