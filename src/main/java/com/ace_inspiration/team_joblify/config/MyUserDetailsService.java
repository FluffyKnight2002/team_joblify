package com.ace_inspiration.team_joblify.config;

import com.ace_inspiration.team_joblify.entity.User;
import com.ace_inspiration.team_joblify.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        if (user != null) {

            return new MyUserDetails(user) {
            };

            

        }
        throw new UsernameNotFoundException("User Not Found");
    }
}
