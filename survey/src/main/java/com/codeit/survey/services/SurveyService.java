package com.codeit.survey.services;

import com.codeit.survey.DTOs.DTOService.SurveyDTOService;
import com.codeit.survey.DTOs.EntityDTOs.SurveyDTO;
import com.codeit.survey.DTOs.EntitiyListDTOs.SurveysDTO;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.SurveyRepo;
import com.codeit.survey.services.adminServices.SurveyServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class SurveyService {
    private SurveyRepo surveyRepo;
    private UserService userService;
    private VerificationService verificationService;
    private SurveyServiceAdmin surveyServiceAdmin;
    private SurveyDTOService surveyDTOService;


    @Autowired
    public SurveyService(SurveyRepo surveyRepo, UserService userService, VerificationService verificationService, SurveyServiceAdmin surveyServiceAdmin, SurveyDTOService surveyDTOService){
        this.surveyRepo = surveyRepo;
        this.userService = userService;
        this.verificationService = verificationService;
        this.surveyServiceAdmin = surveyServiceAdmin;
        this.surveyDTOService = surveyDTOService;
    }

    public ResponseEntity<?> getSurveysByUserId_response(Integer userId){
        SurveyUser surveyUser = userService.getUserById(userId);
        if(surveyUser == null){
            return ResponseEntity.badRequest().build();
        }
        List<Survey> surveys = surveyRepo.findSurveysBySurveyUser(surveyUser);
        return ResponseEntity.ok(surveyDTOService.createDTOFromSurveys(surveys));
    }

    public Survey findById(Integer id){
        return surveyRepo.findById(id).orElse(null);
    }

     List<Survey> getSurveysByUserId(Integer userId){
        SurveyUser surveyUser = userService.getUserById(userId);
        return surveyRepo.findSurveysBySurveyUser(surveyUser);
    }



    public ResponseEntity<?> getSurveyById(Integer surveyId){
        if(verificationService.notUserSurvey(surveyId)){
            return ResponseEntity.badRequest().build();
        }
        return surveyServiceAdmin.getSurveyById(surveyId);
    }
    public ResponseEntity<?> checkAndAddSurvey(Survey survey){
        return surveyServiceAdmin.checkAndAddSurvey(survey);
    }

    public ResponseEntity<?> checkAndPublishSurvey(Integer surveyId){
        if(verificationService.notUserSurvey(surveyId)){
            return ResponseEntity.badRequest().build();
        }
        return surveyServiceAdmin.checkAndPublishSurvey(surveyId);
    }

    public ResponseEntity<?> deleteSurveyById(Integer surveyId){
        if (verificationService.notUserSurvey(surveyId)){
            return ResponseEntity.badRequest().build();
        }
        return surveyServiceAdmin.deleteSurveyById(surveyId);
    }

    public ResponseEntity<?> checkAndUpdateSurvey(Survey newSurvey){
        if (verificationService.notUserSurvey(newSurvey.getId())){
            return ResponseEntity.badRequest().build();
        }
        return surveyServiceAdmin.checkAndUpdateSurvey(newSurvey);
    }


}
