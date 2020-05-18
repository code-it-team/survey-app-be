package com.codeit.survey.services;

import com.codeit.survey.DTOs.DTOService.SurveyDTOService;
import com.codeit.survey.aspects.annotations.VerifySurveyBelongToUser;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.SurveyRepo;
import com.codeit.survey.services.adminServices.SurveyServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


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

    @VerifySurveyBelongToUser
    public ResponseEntity<?> checkAndPublishSurvey(Integer surveyId, String clientURL){
        return surveyServiceAdmin.checkAndPublishSurvey(surveyId, clientURL);
    }

    @VerifySurveyBelongToUser
    public ResponseEntity<?> deleteSurveyById(Integer surveyId){
        return surveyServiceAdmin.deleteSurveyById(surveyId);
    }

    @VerifySurveyBelongToUser
    public ResponseEntity<?> checkAndUpdateSurvey(Survey newSurvey){
        return surveyServiceAdmin.checkAndUpdateSurvey(newSurvey);
    }

    @VerifySurveyBelongToUser
    public ResponseEntity<?> getSurveyPublicationLink(Integer surveyId){
        return surveyServiceAdmin.getSurveyPublicationLink(surveyId);
    }


}
