package com.codeit.survey.security;

import com.codeit.survey.entities.SurveyUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private  String userName;
    private String password;
    private boolean enabled;
    private List<GrantedAuthority> authorities;


    public CustomUserDetails(SurveyUser user) {
        this.userName = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.isEnabled();

        authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getAuthority().getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
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
        return this.enabled;
    }

}

