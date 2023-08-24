package com.ace_inspiration.team_joblify.controller;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.EmailTemplateDto;
import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.Candidate;
import com.ace_inspiration.team_joblify.entity.Interview;
import com.ace_inspiration.team_joblify.entity.InterviewStage;
import com.ace_inspiration.team_joblify.entity.InterviewType;
import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.entity.Role;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.InterviewRepository;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.repository.VacancyInfoRepository;
import com.ace_inspiration.team_joblify.service.DepartmentService;
import com.ace_inspiration.team_joblify.service.EmailService;
import com.ace_inspiration.team_joblify.service.InterviewService;
import com.ace_inspiration.team_joblify.service.OtpService;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class Api {
	private final CandidateService candidateService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final OtpService otpService;
    private final DepartmentService departmentService;
    private final InterviewService interService;

    @GetMapping("/get-all-user")
    public DataTablesOutput<User> getALlUsers(DataTablesInput input) {
        return userRepository.findAll(input);
    }

    @PostMapping(value = "/user-register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> userRegister(UserDto userDto, Authentication authentication) throws IOException {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userService.userCreate(userDto, myUserDetails.getUserId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/user-profile-edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> userProfileEdit(UserDto userDto, @RequestParam("userId") long userId, Authentication authentication) throws IOException {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        if (myUserDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(Role.DEFAULT_HR.name()))) {
            User user = userService.adminProfileEdit(userDto, userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            User user = userService.userProfileEdit(userDto, userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

    }

    @PostMapping("/change-password")
    public boolean changePassword(@RequestParam("newPassword") String newPassword, @RequestParam("email") String email) {
        return userService.passwordChange(newPassword, email);
    }

    @PostMapping("/old-password-check")
    public boolean oldPasswordCheck(@RequestParam("oldPassword") String oldPassword, @RequestParam("email") String email) {
        return userService.checkOldPassword(oldPassword, email);
    }

    @PostMapping("/sendOTP")
    public String sendEmail(@RequestBody EmailTemplateDto emailTemplateDto) {
        emailService.sendForgetPasswordEmail(emailTemplateDto.getTo());
        return "Email sent successfully!";
    }

    @PostMapping("/send-invite-email")
    public boolean sendInviteEmail(@RequestBody EmailTemplateDto emailTemplateDto) {
       boolean email=emailService.sendJobOfferEmail(emailTemplateDto);
        if(email==true) {
        	interService.saveInterview(emailTemplateDto);
        	candidateService.stage(emailTemplateDto.getCanId());
        	return true;
        }else {
        	return false;
        }
        
    }

    @PostMapping("/otp-submit")
    public boolean otpSubmit(@RequestParam("otp") String otp, @RequestParam("email") String email) {
        return otpService.otpCheck(otp, email);
    }

    @PostMapping("/search-email")
    public boolean otpSubmit(@RequestParam("email") String email) {
        User user = otpService.emailCheck(email);
        return user != null;

    }

    @PostMapping("/all-department")
    public List<Department> allDepartment() {
        return departmentService.selectAllDepartment();
    }

    @PostMapping("/suspend")
    public boolean suspend(@RequestParam("id")long id){
        return userService.suspend(id);
    }

    @PostMapping("/activate")
    public boolean activate(@RequestParam("id")long id){
        return userService.activate(id);
    }

    @PostMapping("/get-user-profile")
    public User userProfileData(@RequestParam ("id") long id){
        return userService.findById(id).orElseThrow(()-> new NoSuchElementException("User Not Found."));
    }


//    @GetMapping("/filtered-vacancies")
//    public List <Object[]> getFilteredVacancies() {
//        List<Object[]> result = vacancyInfoRepository.vacancyFilter("recent", true, null, "BOTH", null, false, "anytime", 1, 1);
//        System.out.println(result);
//
//        return result;
//    }
}