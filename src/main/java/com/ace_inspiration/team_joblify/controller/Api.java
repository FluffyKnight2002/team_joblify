package com.ace_inspiration.team_joblify.controller;

import com.ace_inspiration.team_joblify.config.FirstDaySpecificationUser;
import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.controller.hr.NotificationCreator;
import com.ace_inspiration.team_joblify.dto.EmailTemplateDto;
import com.ace_inspiration.team_joblify.dto.SummaryDto;
import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.entity.Role;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.service.*;
import com.ace_inspiration.team_joblify.service.candidate_service.CandidateService;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
    private final NotificationCreator notificationCreator;
    private final PasswordEncoder passwordEncoder;

    private FirstDaySpecificationUser firstDaySpecificationUser;

    private final OfferMailSendedService offerMailSendedService;

    @GetMapping("/get-all-user")
    public DataTablesOutput<User> getAllUsers(@Valid DataTablesInput input) {
        System.out.println(input);
        DataTablesOutput<User> user = userRepository.findAll(input);
        firstDaySpecificationUser = new FirstDaySpecificationUser(input);

        System.out.println(input);

        if (firstDaySpecificationUser == null) {
            return user;
        } else {
            user = userRepository.findAll(input, firstDaySpecificationUser);
            return user;
        }

    }

    @PostMapping(value = "/user-register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> userRegister(UserDto userDto, Authentication authentication) throws IOException {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userService.userCreate(userDto, myUserDetails.getUserId());
        if (user != null) {
            String message = myUserDetails.getName() + " create a new User named " + user.getName();
            String link = "/user-profile-edit?email=" + user.getEmail();
            notificationCreator.createNotification(myUserDetails, message, link);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/user-profile-edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> userProfileEdit(UserDto userDto, @RequestParam("currentEmail") String currentEmail,
            Authentication authentication) throws IOException {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        User user;

        // Check if the authenticated user has the role "Default HR"
        boolean isDefaultHr = myUserDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.DEFAULT_HR.name()));

        boolean isSeniorHr = myUserDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.SENIOR_HR.name()));

        // Check if the user is trying to edit their own profile
        boolean isEditingOwnProfile = currentEmail.equals(myUserDetails.getEmail());

        if (isDefaultHr || isEditingOwnProfile || isSeniorHr) {
            // If Default HR or editing own profile, allow profile edit
            if (isDefaultHr || isSeniorHr) {
                user = userService.adminProfileEdit(userDto, currentEmail);
            } else {
                user = userService.userProfileEdit(userDto, currentEmail);
            }

            if (myUserDetails.getEmail().equals(currentEmail)) {
                MyUserDetails myNewUserDetails = new MyUserDetails(user);
                Authentication newAuthentication = new UsernamePasswordAuthenticationToken(myNewUserDetails,
                        myNewUserDetails.getPassword(), myNewUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(newAuthentication);
            }

            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            // If not Default HR and not editing own profile, return a 403 response
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/change-password")
    public boolean changePassword(@RequestParam("newPassword") String newPassword,
            @RequestParam("email") String email, Authentication authentication) {

        boolean success = userService.passwordChange(newPassword, email);
        if (authentication != null && success) {
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            User user = userService.findById(myUserDetails.getUserId());

            MyUserDetails myNewUserDetails = new MyUserDetails(user);
            Authentication newAuthentication = new UsernamePasswordAuthenticationToken(myNewUserDetails,
                    myNewUserDetails.getPassword(), myNewUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);
            return true;
        } else if (success) {
            return true;
        } else {
            return false;
        }
    }

    @PostMapping("/old-password-check")
    public boolean oldPasswordCheck(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("email") String email) {
        return userService.checkOldPassword(oldPassword, email);
    }

    @PostMapping("/sendOTP")
    public String sendEmail(@RequestBody EmailTemplateDto emailTemplateDto) {
        emailService.sendForgetPasswordEmail(emailTemplateDto.getTo());
        
        return "Email sent successfully!";
    }

    @PostMapping("/send-invite-email")
    public boolean sendInviteEmail(@RequestBody EmailTemplateDto emailTemplateDto, Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        boolean email = emailService.sendJobOfferEmail(emailTemplateDto);

        if (email) {
            interService.saveInterview(emailTemplateDto);
            candidateService.stage(emailTemplateDto.getCanId());

            SummaryDto summaryDto = candidateService.findByid(emailTemplateDto.getCanId());

            String message = myUserDetails.getName() + " send Interview Invite Mail to " + emailTemplateDto.getName();
            String link = "/candidate-view-summary?position=" + summaryDto.getApply_position()+"&candidateId="+summaryDto.getId();
            notificationCreator.createNotification(myUserDetails, message, link);

            return true;
        } else {
            return false;
        }
    }

    @PostMapping("/send-offer-mail")
    public boolean sendOfferMail(@RequestBody EmailTemplateDto emailTemplateDto, Authentication authentication) {
        MyUserDetails myuser = (MyUserDetails) authentication.getPrincipal();
        emailTemplateDto.setUserId(myuser.getUserId());
        System.err.println(emailTemplateDto.getName());
        boolean email = emailService.sendJobOfferEmail(emailTemplateDto);
        if(email){
            offerMailSendedService.setDataInOfferMail(emailTemplateDto);
            candidateService.offer(emailTemplateDto.getCanId());
            SummaryDto summaryDto = candidateService.findByid(emailTemplateDto.getCanId());
            interService.savefirst(emailTemplateDto.getCanId());
            String message = myuser.getName() + " send Offer Mail to " + emailTemplateDto.getName();
            String link = "/candidate-view-summary?position=" + summaryDto.getApply_position()+"&candidateId="+summaryDto.getId();
            notificationCreator.createNotification(myuser, message, link);
            return true;
        }else{
           return false;
        }

    }

    @PostMapping("/otp-submit")
    public boolean otpSubmit(@RequestParam("otp") String otp, @RequestParam("email") String email, HttpSession session) {
        boolean otpCheck = otpService.otpCheck(otp, email);
        if (otpCheck) {
            session.setAttribute("otpChecked", true);
            return true;

        } else {
        
        return false;
    }
    }
    @PostMapping("/search-email")
    public boolean otpSubmit(@RequestParam("email") String email, HttpSession session) {
        User user = otpService.emailCheck(email);
        if (user != null) {
            session.setAttribute("emailSearched", true);
            return true;

        }
        return false;

    }

    @GetMapping("/all-department")
    public List<Department> allDepartment() {
        return departmentService.selectAllDepartment();
    }

    @PostMapping("/suspend")
    public boolean suspend(@RequestParam("id") long id) {
        return userService.suspend(id);
    }

    @PostMapping("/activate")
    public boolean activate(@RequestParam("id") long id) {
        return userService.activate(id);
    }

    @PostMapping("/get-user-profile")
    public User userProfileData(@RequestParam("email") String email) {
        return userService.findByEmail(email);
    }

    @PostMapping("/find-phonenumber-by-email")
    public String findPhoneNumberByEmail(@RequestParam("email") String email) {

        User user = userService.findByEmail(email);
        System.out.println(user.getPhone());
        return user.getPhone();
    }

    @GetMapping("/phone-duplicate")
    public boolean checkPhoneDuplicate(@RequestParam("phone") String phone) {
        return userService.checkPhoneDuplicate(phone);
    }

    @GetMapping("/username-duplicate")
    public boolean checkUsernameDuplicate(@RequestParam("username") String username) {
        return userService.checkUsernameDuplicate(username);
    }

    @GetMapping("/email-duplicate")
    public boolean emailDuplicateSearch(@RequestParam("email") String email) {
        return userService.emailDuplication(email);
    }

    @GetMapping("/phone-duplicate-except-himself")
    public boolean checkPhoneDuplicateExceptHimself(@RequestParam("newPhone") String newPhone,
            @RequestParam("userId") long userId) {
        System.out.println(userId);
        return userService.checkPhoneDuplicateExceptHimself(newPhone, userId);
    }

    @GetMapping("/username-duplicate-except-himself")
    public boolean checkUsernameDuplicateExceptHimself(@RequestParam("newUsername") String newUsername,
            @RequestParam("userId") long userId) {
        return userService.checkUsernameDuplicateExceptHimself(newUsername, userId);
    }

    @GetMapping("/email-duplicate-except-himself")
    public boolean emailDuplicateSearchExceptHimself(@RequestParam("newEmail") String newEmail,
            @RequestParam("userId") long userId) {
        return userService.emailDuplicationExceptHimself(newEmail, userId);
    }

    @PostMapping("/authenticated-user-data")
    public Object loginUserData(Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        String enteredPassword = "ace1122121"; // Replace this with the entered password
        boolean passwordMatches = passwordEncoder.matches(enteredPassword, myUserDetails.getPassword());

        return new Object[] { myUserDetails, passwordMatches };
    }

    @GetMapping("/getCookies")
    public boolean getCookieValue(HttpServletRequest request, HttpServletResponse response) {
        String cookieName = "remember-me"; // Change this to the name of the cookie you're looking for

        Cookie[] cookies = request.getCookies(); // Get all cookies from the request

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    // Set the maxAge of the cookie to 2 weeks (in seconds)
                    int maxAgeInSeconds = 14 * 24 * 60 * 60;
                    cookie.setMaxAge(maxAgeInSeconds);

                    response.addCookie(cookie); // Update the cookie in the response
                    return true; // Return the name of the page to show cookie expiration message
                }
            }
        }

        // Cookie not found, handle accordingly
        return false; // Return the name of the page to display cookie not found message
    }

    @PostMapping("/contact-us")
    public boolean sendDirectEmail(@RequestParam("name")String name,
                                   @RequestParam("email")String email,
                                   @RequestParam("about")String about,
                                   @RequestParam("message")String message) {
        if(emailService.sendDirectMail(name, email, about, message)) {
            return true;
        }
        return false;
    }

    // @GetMapping("/filtered-vacancies")
    // public List <Object[]> getFilteredVacancies() {
    // List<Object[]> result = vacancyInfoRepository.vacancyFilter("recent", true,
    // null, "BOTH", null, false, "anytime", 1, 1);
    // System.out.println(result);
    //
    // return result;
    // }
}