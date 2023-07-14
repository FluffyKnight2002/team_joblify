package com.ace_inspiration.team_joblify.controller;

import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class Api {

    private final UserRepository userRepository;
    @GetMapping("/get-user-data")
    public DataTablesOutput<User> allUserList(@Valid DataTablesInput input) {
        return userRepository.findAll(input);
    }
}
