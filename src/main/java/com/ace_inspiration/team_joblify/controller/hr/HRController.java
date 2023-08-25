package com.ace_inspiration.team_joblify.controller.hr;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.service_implement.candidate_service_implement.CandidateServiceImplement;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HRController {

    private final CandidateServiceImplement candidateImpl;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("currentPage", "/dashboard");
        return "dashboard";
    }

    @GetMapping("/otp")
    public String otp() {
        return "otp";
    }

    @GetMapping("/invite-mail")
    public String inviteMail() {
        return "invite-mail";
    }

   
}
