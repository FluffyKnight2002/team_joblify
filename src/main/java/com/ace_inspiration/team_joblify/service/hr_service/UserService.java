package com.ace_inspiration.team_joblify.service.hr_service;

import com.ace_inspiration.team_joblify.dto.UserDto;
import com.ace_inspiration.team_joblify.entity.User;

import java.io.IOException;

public interface UserService {
    User userCreate(UserDto userDto, long userId) throws IOException;
}
