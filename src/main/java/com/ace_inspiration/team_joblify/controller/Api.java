package com.ace_inspiration.team_joblify.controller;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.controller.hr.NotificationCreator;
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
import com.ace_inspiration.team_joblify.service.NotificationService;
import com.ace_inspiration.team_joblify.service.OtpService;
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

@RestController
@RequiredArgsConstructor
public class Api {

    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final OtpService otpService;
    private final DepartmentService departmentService;
    private final InterviewRepository inter;
    private final NotificationCreator notificationCreator;

    @GetMapping("/get-all-user")
    public DataTablesOutput<User> getALlUsers(DataTablesInput input) {
        return userRepository.findAll(input);
    }

    @PostMapping(value = "/user-register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> userRegister(UserDto userDto, Authentication authentication) throws IOException {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userService.userCreate(userDto, myUserDetails.getUserId());
        if(user != null){
            String message = myUserDetails.getName() + " create a new User named" + user.getName();
            String link = "/user-profile-edit?id=" + user.getId();
            notificationCreator.createNotification(myUserDetails,message,link);
        }
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
    public String sendInviteEmail(@RequestBody EmailTemplateDto emailTemplateDto) {
    	System.err.println(">>>>>>>>>"+emailTemplateDto.getCanId()+">>>"+InterviewType.valueOf(emailTemplateDto.getType()));
        emailService.sendJobOfferEmail(emailTemplateDto.getTo(), emailTemplateDto.getContent());

        Candidate candidate = new Candidate();
        candidate.setId(emailTemplateDto.getCanId());

        Interview interview=new Interview();
        interview.setInterviewDate(emailTemplateDto.getDate());
        interview.setInterviewTime(emailTemplateDto.getTime());
        interview.setType(InterviewType.valueOf(emailTemplateDto.getType()));
        interview.setInterviewStage(InterviewStage.valueOf(emailTemplateDto.getStatus()));
        interview.setCandidate(candidate);
        inter.save(interview);

        return "Email sent successfully!";
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
    public User userProfileData(@RequestParam ("email") String email){
        return userService.findByEmail(email);
    }

    @PostMapping("/find-phonenumber-by-email")
    public String findPhoneNumberByEmail(@RequestParam ("email") String email){
        
        User user = userService.findByEmail(email);
        System.out.println(user.getPhone());
        return user.getPhone();
    }

    @GetMapping("/phone-duplicate")
    public boolean checkPhoneDuplicate(@RequestParam("phone")String phone){
        return userService.checkPhoneDuplicate(phone);
    }


    @GetMapping("/username-duplicate")
    public boolean checkUsernameDuplicate(@RequestParam("username")String username){
        return userService.checkUsernameDuplicate(username);
    }

    @GetMapping("/email-duplicate")
    @ResponseBody
    public boolean emailDuplicateSearch(@RequestParam("email") String email) {
        return userService.emailDuplication(email);
}

//    @GetMapping("/filtered-vacancies")
//    public List <Object[]> getFilteredVacancies() {
//        List<Object[]> result = vacancyInfoRepository.vacancyFilter("recent", true, null, "BOTH", null, false, "anytime", 1, 1);
//        System.out.println(result);
//
//        return result;
//    }
}