package com.codeit.survey.controllers.AdminControllers;

import com.codeit.survey.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminSurveyController {
    private SurveyService surveyService;

    @Autowired
    public AdminSurveyController(SurveyService surveyService){
        this.surveyService = surveyService;
    }


    @DeleteMapping("/admin/deleteSurvey")
    public ResponseEntity<?> deleteSurveyById(@RequestParam Integer surveyId){
        return surveyService.deleteSurveyById_admin(surveyId);
    }




}
