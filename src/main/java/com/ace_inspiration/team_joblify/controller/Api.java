package com.ace_inspiration.team_joblify.controller;

import com.ace_inspiration.team_joblify.config.MyUserDetails;
import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class Api {

    private final UserRepository userRepository;
    private final UserService userService;
    @GetMapping("/get-all-user")
    public DataTablesOutput<UserDto> allUserList(DataTablesInput input) {
        DataTablesOutput<User> users = userRepository.findAll(input);
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users.getData()) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setPhone(user.getPhone());
            userDto.setAddress(user.getAddress());
            userDto.setDepartment(user.getDepartment().getName());
            userDtos.add(userDto);
        }

        DataTablesOutput<UserDto> userDtoDataTablesOutput = new DataTablesOutput<>();
        userDtoDataTablesOutput.setData(userDtos);
        userDtoDataTablesOutput.setDraw(users.getDraw());
        userDtoDataTablesOutput.setError(users.getError());
        userDtoDataTablesOutput.setSearchPanes(users.getSearchPanes());
        userDtoDataTablesOutput.setRecordsFiltered(users.getRecordsFiltered());
        userDtoDataTablesOutput.setRecordsTotal(users.getRecordsTotal());


        return userDtoDataTablesOutput;
    }

    @PostMapping(value = "/user-register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> userProfileEdit(UserDto userDto, Authentication authentication)throws IOException {
        MyUserDetails myUserDetails= (MyUserDetails)authentication.getPrincipal();
        User user=userService.userCreate(userDto, myUserDetails.getUserId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
