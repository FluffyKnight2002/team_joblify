package com.ace_inspiration.team_joblify.controller;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.Role;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.UserRepository;
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

@RestController
@RequiredArgsConstructor
public class Api {

    private final UserRepository userRepository;
    private final UserService userService;


    @GetMapping("/get-all-user")
    public DataTablesOutput<User> getALlUsers(DataTablesInput input){
        return userRepository.findAll(input);
    }

    @PostMapping(value = "/user-register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> userRegister(UserDto userDto, Authentication authentication)throws IOException {
        MyUserDetails myUserDetails= (MyUserDetails)authentication.getPrincipal();
        User user=userService.userCreate(userDto, myUserDetails.getUserId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/user-profile-edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> userProfileEdit(UserDto userDto, @RequestParam("userId")long userId, Authentication authentication)throws IOException {
        MyUserDetails myUserDetails= (MyUserDetails)authentication.getPrincipal();
        if (myUserDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(Role.DEFAULT_HR.name()))) {
            User user=userService.adminProfileEdit(userDto, userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            User user=userService.userProfileEdit(userDto, userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

    }

    @PostMapping("/change-password")
    public boolean changePassword(@RequestParam ("newPassword") String newPassword, @RequestParam ("id") long id){

        return userService.passwordChange(newPassword, id);
    }

    @PostMapping("/old-password-check")
    public boolean oldPasswordCheck(@RequestParam("oldPassword") String oldPassword, @RequestParam("id") Long id){

        return userService.checkOldPassword(oldPassword, id);

    }
}
