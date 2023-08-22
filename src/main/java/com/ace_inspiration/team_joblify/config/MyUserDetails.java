package com.ace_inspiration.team_joblify.config;

import com.ace_inspiration.team_joblify.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Data
public class MyUserDetails implements UserDetails {

    private  final User user;


    public long getUserId(){
        return user.getId();
    }

    public String getName(){
        return user.getName();
    }

    public String getEmail(){
        return user.getEmail();
    }

    public String getPhone(){
        return user.getPhone();
    }

    public String getPhoto(){
        return user.getPhoto();
    }

    public String getDepartment(){
        return user.getDepartment().getName();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
