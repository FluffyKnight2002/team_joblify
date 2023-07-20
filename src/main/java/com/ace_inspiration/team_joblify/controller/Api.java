package com.ace_inspiration.team_joblify.controller;

import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/v1")
public class Api {

    private final UserRepository userRepository;
//    private final UserService userService;
    @GetMapping("/get-user-data")
    public DataTablesOutput<User> allUserList(@Valid DataTablesInput input) {
        return userRepository.findAll(input);
    }

//    @PostMapping(value = "/user-register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<User> userProfileEdit(UserDto userDto, Authentication authentication)throws IOException {
//        MyUserDetails myUserDetails= (MyUserDetails)authentication.getPrincipal();
//        System.out.println(myUserDetails.getUserId() + "aaa");
//        User user=userService.userCreate(userDto, myUserDetails.getUserId());
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }
}
