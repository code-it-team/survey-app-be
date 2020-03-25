package com.codeit.survey.services;

import com.codeit.survey.DTOs.AuthenticationResponse;
import com.codeit.survey.entities.SurveyUser;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private UserService userService;

    @Autowired
    public AuthenticationService(UserService userService){
        this.userService = userService;
    }

    public AuthenticationResponse createAuthenticationResponseFromUser(String jwt, SurveyUser surveyUser){
        return new AuthenticationResponse(
                jwt,
                userService.createDTOFromSurveyUser(surveyUser)
        );
    }

}
