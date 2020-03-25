package com.codeit.survey.services;

import com.codeit.survey.DTOs.EntityDTOs.AuthorityDTO;
import com.codeit.survey.DTOs.EntityDTOs.SurveyUserDTO;
import com.codeit.survey.entities.Authority;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.AuthorityRepo;
import com.codeit.survey.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService {
    private UserRepo userRepo;
    private AuthorityRepo authorityRepo;

    @Autowired
    public UserService(UserRepo userRepo, AuthorityRepo authorityRepo){
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
    }

    public SurveyUserDTO createDTOFromSurveyUser(SurveyUser surveyUser){
        return new SurveyUserDTO(
                surveyUser.getId(),
                surveyUser.getUsername(),
                Arrays.asList(
                        new AuthorityDTO(
                                surveyUser.getAuthority().getId(),
                                surveyUser.getAuthority().getRole())
                )
        );
    }

    public ResponseEntity<?> addUser(SurveyUser surveyUser){
        try{
            // check for username existence
            if(userRepo.findByUsername(surveyUser.getUsername()).isPresent()){
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            Authority authority = authorityRepo.findAuthorityByRole(surveyUser.getAuthority().getRole());
            surveyUser.setAuthority(authority);
            return ResponseEntity.ok( // return an OK
                    createDTOFromSurveyUser( // create a DTO for it
                            userRepo.save(surveyUser) // save the new user
                    )
            );
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    public SurveyUser getSurveyUserByUserName(String userName){
        return userRepo.findByUsername(userName).orElse(null);
    }

}
