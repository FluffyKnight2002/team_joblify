package com.ace_inspiration.team_joblify.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.entity.Role;
import com.ace_inspiration.team_joblify.entity.Status;

import net.sf.jasperreports.engine.JRException;

public interface ReportService {
    public ResponseEntity<byte[]> interviewProcess(String format, LocalDate startDate, 
    LocalDate endDate, String department, String position) throws JRException, IOException;

    public ResponseEntity<byte[]> allCandidate(String format, LocalDate startDate, 
    LocalDate endDate, String position, List<Level> level, String selectionStatus, String interviewStatus) throws JRException, IOException;
}
