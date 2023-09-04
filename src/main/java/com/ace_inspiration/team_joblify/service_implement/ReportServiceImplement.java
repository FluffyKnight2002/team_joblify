package com.ace_inspiration.team_joblify.service_implement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
import com.ace_inspiration.team_joblify.entity.InterviewProcess;
import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.entity.Role;
import com.ace_inspiration.team_joblify.entity.Status;
import com.ace_inspiration.team_joblify.repository.AllCandidatesReportRepository;
import com.ace_inspiration.team_joblify.repository.AllPostRepository;
import com.ace_inspiration.team_joblify.service.ReportService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ReportServiceImplement implements ReportService {

  private final AllPostRepository allPostRepository;

  private final AllCandidatesReportRepository allCandidatesReportRepository;

  private final EntityManager entityManager;

  @Override
  public ResponseEntity<byte[]> interviewProcess(String format, LocalDate startDate,
      LocalDate endDate, String department, String position) throws JRException, IOException {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AllPost> criteriaQuery = criteriaBuilder.createQuery(AllPost.class);
    Root<AllPost> root = criteriaQuery.from(AllPost.class);
    List<Predicate> predicates = new ArrayList<>();



    if (startDate != null && endDate != null) {
      predicates.add(criteriaBuilder.between(root.get("openDate"), startDate, endDate));
    } else if (startDate != null) {
      predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("openDate"), startDate));
    } else if (endDate != null) {
      predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("openDate"), endDate));
    }
    
    // Check and add predicates for department
    if (!department.isEmpty()) {
      predicates.add(criteriaBuilder.equal(root.get("department"), department));
    }

    // Check and add predicates for position
    if (!position.isEmpty()) {
      predicates.add(criteriaBuilder.equal(root.get("position"), position));
    }

    // Combine all predicates using AND
    if (!predicates.isEmpty()) {
      criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
  }

  TypedQuery<AllPost> query = entityManager.createQuery(criteriaQuery);

  List<AllPost> allPosts;
  if (predicates.isEmpty()) {
      // If all values are null, find all records
      CriteriaQuery<AllPost> selectAllQuery = criteriaBuilder.createQuery(AllPost.class);
      Root<AllPost> selectAllRoot = selectAllQuery.from(AllPost.class);
      allPosts = entityManager.createQuery(selectAllQuery.select(selectAllRoot)).getResultList();
  } else {
      allPosts = query.getResultList();
  }

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
  public ResponseEntity<byte[]> allCandidate(String format, LocalDate startDate, LocalDate endDate, String position,
      List<Level> level, Status selectionStatus, Status interviewStatus) throws JRException, IOException {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AllCandidatesReport> criteriaQuery = criteriaBuilder.createQuery(AllCandidatesReport.class);
    Root<AllCandidatesReport> root = criteriaQuery.from(AllCandidatesReport.class);
    List<Predicate> predicates = new ArrayList<>();

        System.out.println(level.size());

    // Check if all parameters are null, and if so, return all records
    if (startDate != null && endDate != null) {
      predicates.add(criteriaBuilder.between(root.get("applyDate"), startDate, endDate));
    } else if (startDate != null) {
      predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("applyDate"), startDate));
    } else if (endDate != null) {
      predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("applyDate"), endDate));
    }

  

    if (!position.isEmpty()) {
      predicates.add(criteriaBuilder.equal(root.get("applyPosition"), position));
    }

    if (level != null && !level.isEmpty()) { // Check if level is not empty
      List<Predicate> levelPredicates = new ArrayList<>();
      level.forEach(lvl -> {
          levelPredicates.add(criteriaBuilder.equal(root.get("lvl"), lvl)); // Use 'equal' for each level
      });
      predicates.add(criteriaBuilder.or(levelPredicates.toArray(new Predicate[0]))); // Combine level predicates with 'or'
  }

    if (selectionStatus != null) {
      predicates.add(criteriaBuilder.equal(root.get("selectionStatus"), selectionStatus));
    }

    if (interviewStatus != null) {
      predicates.add(criteriaBuilder.equal(root.get("interviewStatus"), interviewStatus));
    }
    if (!predicates.isEmpty()) {
      criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
  }

  TypedQuery<AllCandidatesReport> query = entityManager.createQuery(criteriaQuery);

  List<AllCandidatesReport> allCandidatesReports;
  if (predicates.isEmpty()) {
      // If all values are null, find all records
      CriteriaQuery<AllCandidatesReport> selectAllQuery = criteriaBuilder.createQuery(AllCandidatesReport.class);
      Root<AllCandidatesReport> selectAllRoot = selectAllQuery.from(AllCandidatesReport.class);
      allCandidatesReports = entityManager.createQuery(selectAllQuery.select(selectAllRoot)).getResultList();
  } else {
      allCandidatesReports = query.getResultList();
  }

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
