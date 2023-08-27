package com.ace_inspiration.team_joblify.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;

import net.sf.jasperreports.engine.JRException;

public interface ReportService {
    public ResponseEntity<byte[]> interviewProcess(String format) throws JRException, IOException;

    public ResponseEntity<byte[]> allCandidate(String format) throws JRException, IOException;
}
