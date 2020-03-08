package com.codeit.survey.services;

import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.UserRepo;
import com.codeit.survey.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SurveyUser user = userRepo.findByUsername(userName).orElse(null);

        if (user != null) {
            return new CustomUserDetails(user);
        }else{
            return null;
        }

    }
}
