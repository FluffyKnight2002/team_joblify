package com.ace_inspiration.team_joblify.service_implement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.ace_inspiration.team_joblify.entity.AllCandidatesReport;
import com.ace_inspiration.team_joblify.entity.AllPost;
import com.ace_inspiration.team_joblify.repository.AllCandidatesReportRepository;
import com.ace_inspiration.team_joblify.repository.AllPostRepository;
import com.ace_inspiration.team_joblify.service.ReportService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class ReportServiceImplement implements ReportService {
  @Autowired
  private AllPostRepository allPostRepository;

  @Autowired
  private AllCandidatesReportRepository allCandidatesReportRepository;

  @Override
  public ResponseEntity<byte[]> interviewProcess(String format) throws JRException, IOException {

    List<AllPost> allPosts = allPostRepository.findAll();
    System.err.println(allPosts);

    File file = ResourceUtils.getFile("classpath:interview_process.jrxml");
    JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(allPosts);
    Map<String, Object> parameters = new HashMap<>();

    // parameters.put("createdBy", "Interview Process");
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    if (format.equalsIgnoreCase("pdf")) {
      JRPdfExporter exporterPdf = new JRPdfExporter();
      exporterPdf.setExporterInput(new SimpleExporterInput(jasperPrint));
      exporterPdf.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
      exporterPdf.exportReport();

      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Disposition", "attachment; filename=Interview_Process.pdf");

      return new ResponseEntity<>(outStream.toByteArray(), headers, HttpStatus.OK);
    } else if (format.equalsIgnoreCase("excel")) {
      JRXlsxExporter exporterXLS = new JRXlsxExporter();
      exporterXLS.setExporterInput(new SimpleExporterInput(jasperPrint));
      exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
      exporterXLS.exportReport();

      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Disposition", "attachment; filename=Interview_Process.xlsx");

      return new ResponseEntity<>(outStream.toByteArray(), headers, HttpStatus.OK);
    }
    return null;
  }

  @Override
  public ResponseEntity<byte[]> allCandidate(String format) throws JRException, IOException {

    List<AllCandidatesReport> allCandidatesReports = allCandidatesReportRepository.findAll();
    System.err.println(allCandidatesReports);

    File file = ResourceUtils.getFile("classpath:all_candidates.jrxml");
    JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(allCandidatesReports);
    Map<String, Object> parameters = new HashMap<>();

    // parameters.put("createdBy", "Interview Process");
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    if (format.equalsIgnoreCase("pdf")) {
      JRPdfExporter exporterPdf = new JRPdfExporter();
      exporterPdf.setExporterInput(new SimpleExporterInput(jasperPrint));
      exporterPdf.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
      exporterPdf.exportReport();

      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Disposition", "attachment; filename=All_Candidates.pdf");

      return new ResponseEntity<>(outStream.toByteArray(), headers, HttpStatus.OK);
    } else if (format.equalsIgnoreCase("excel")) {
      JRXlsxExporter exporterXLS = new JRXlsxExporter();
      exporterXLS.setExporterInput(new SimpleExporterInput(jasperPrint));
      exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
      exporterXLS.exportReport();

      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Disposition", "attachment; filename=All_Candidates.xlsx");

      return new ResponseEntity<>(outStream.toByteArray(), headers, HttpStatus.OK);
    }
    return null;
  }
}
