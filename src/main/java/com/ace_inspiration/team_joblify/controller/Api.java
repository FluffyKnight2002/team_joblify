package com.ace_inspiration.team_joblify.controller;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.EmailTemplateDto;
import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.Role;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.service.EmailService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class Api {

    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final OtpService otpService;

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
    public boolean changePassword(@RequestParam("newPassword") String newPassword, @RequestParam("id") long id) {

        return userService.passwordChange(newPassword, id);
    }

    @PostMapping("/old-password-check")
    public boolean oldPasswordCheck(@RequestParam("oldPassword") String oldPassword, @RequestParam("id") Long id) {

        return userService.checkOldPassword(oldPassword, id);

    }

    @PostMapping("/sendOTP")
    public String sendEmail(@RequestBody EmailTemplateDto emailTemplateDto) {
        String otp = UUID.randomUUID().toString().replaceAll("[^A-Z0-9]", "").substring(0, 6);

        emailService.sendForgetPasswordEmail(emailTemplateDto.getTo(), emailTemplateDto.getName(), otp);
        return "Email sent successfully!";
    }

    @PostMapping("/send-invite-email")
    public String sendInviteEmail(@RequestBody EmailTemplateDto emailTemplateDto) {

        emailService.sendJobOfferEmail(emailTemplateDto.getTo(), emailTemplateDto.getContent());
        return "Email sent successfully!";
    }

    @PostMapping("/otp-submit")
    public boolean otpSubmit(@RequestParam("otp") String otp, @RequestParam("userId") long userId) {
        return otpService.otpCheck(otp, userId);
    }

    @PostMapping("/search-email")
    public List<Object> otpSubmit(@RequestParam("email") String email) {
        User user = otpService.emailCheck(email);
        List<Object> object = new ArrayList<>();
        if (user != null) {
            UUID uuid = UUID.randomUUID();
            String otp = uuid.toString().substring(0, 6).toUpperCase();

            otpService.saveOtp(otp, user.getId());
            object.add(true);
            object.add(user.getId());

        } else {
            object.add(false);
            object.add(null);
        }
        return object;
    }
}
