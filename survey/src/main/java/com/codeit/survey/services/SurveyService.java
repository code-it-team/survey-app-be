package com.codeit.survey.services;

import com.codeit.survey.DTOs.EntityDTOs.SurveyDTO;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.SurveyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class SurveyService {
    private SurveyRepo surveyRepo;
    private UserService userService;


    @Autowired
    public SurveyService(SurveyRepo surveyRepo, UserService userService){
        this.surveyRepo = surveyRepo;
        this.userService = userService;
    }

    public SurveyDTO createDTOFromSurvey(Survey survey){
        return new SurveyDTO(
                survey.getId(),
                survey.getCreationDate(),
                survey.getName(),
                userService.createDTOFromSurveyUser(survey.getSurveyUser())
                );
    }


    public ResponseEntity<?> addSurvey(Survey survey){
        try{

            survey.setCreationDate(java.time.LocalDateTime.now());

            surveyRepo.save(survey);
            return ResponseEntity.ok().build();

        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> getSurveyByUser(SurveyUser surveyUser){
        Survey survey = surveyRepo.findSurveyBySurveyUser(surveyUser);

        if (survey != null){
            return ResponseEntity.ok(createDTOFromSurvey(survey));
        }
        return ResponseEntity.badRequest().build();
    }

}
