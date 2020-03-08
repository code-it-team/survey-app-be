package com.codeit.survey.services;

import com.codeit.survey.DTOs.EntityDTOs.AuthorityDTO;
import com.codeit.survey.DTOs.EntityDTOs.SurveyUserDTO;
import com.codeit.survey.entities.Authority;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.AuthorityRepo;
import com.codeit.survey.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService {
    private UserRepo userRepo;
    private AuthorityRepo authorityRepo;
    private SurveyUserDTO surveyUserDTO;

    @Autowired
    public UserService(UserRepo userRepo, AuthorityRepo authorityRepo, SurveyUserDTO surveyUserDTO){
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.surveyUserDTO = surveyUserDTO;
    }

    public SurveyUserDTO addUser(SurveyUser surveyUser){
        Authority authority = authorityRepo.findAuthorityByRole(surveyUser.getAuthority().getRole());
        surveyUser.setAuthority(authority);
        return surveyUserDTO.createFromSurveyUser(userRepo.save(surveyUser));
    }


}
