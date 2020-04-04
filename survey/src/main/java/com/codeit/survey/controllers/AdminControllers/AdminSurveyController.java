package com.codeit.survey.controllers.AdminControllers;

import com.codeit.survey.entities.Survey;
import com.codeit.survey.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/admin/updateSurvey")
    public ResponseEntity<?> updateSurvey(@RequestBody Survey survey){
        return surveyService.updateSurvey_admin(survey);
    }




}
