package com.codeit.survey.services;

import com.codeit.survey.DTOs.EntitiyListDTOs.SurveyUsersDTO;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
                Collections.singletonList(
                        new AuthorityDTO(
                                surveyUser.getAuthority().getId(),
                                surveyUser.getAuthority().getRole())
                )
        );
    }
    private SurveyUsersDTO createDTOFromSurveyUsers(List<SurveyUser> surveyUsers){
        return new SurveyUsersDTO(
                surveyUsers.
                        stream().
                        map(this::createDTOFromSurveyUser).
                        collect(Collectors.toList())
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
            SurveyUserDTO dtoFromSurveyUser = createDTOFromSurveyUser(userRepo.save(surveyUser));
            return ResponseEntity.ok(dtoFromSurveyUser);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    public SurveyUser getSurveyUserByUserName(String userName){
        return userRepo.findByUsername(userName).orElse(null);
    }

    public ResponseEntity<?> getUsersDTO(){
         return ResponseEntity.ok(
                 createDTOFromSurveyUsers(userRepo.findAll())
         );
    }

    public ResponseEntity<?> deleteUserByUserName(String username){
        SurveyUser surveyUser = userRepo.findByUsername(username).orElse(null);
        if (surveyUser == null){
            return ResponseEntity.badRequest().build();
        }
        if (surveyUser.getAuthority().getRole().equals("ROLE_ADMIN")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // delete
        userRepo.delete(surveyUser);
        return ResponseEntity.ok().build();
    }

    public SurveyUser getUserById(Integer id){
        return userRepo.findById(id).orElse(null);
    }

    public ResponseEntity<?> getUserDTOById(Integer id){
        SurveyUser userById = getUserById(id);
        return ResponseEntity.ok(createDTOFromSurveyUser(userById));
    }
}
