package com.codeit.survey.controllers.AdminControllers;

import com.codeit.survey.entities.Survey;
import com.codeit.survey.services.adminServices.SurveyServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AdminSurveyController {
    private SurveyServiceAdmin surveyServiceAdmin;

    @Autowired
    public AdminSurveyController(SurveyServiceAdmin surveyServiceAdmin){
        this.surveyServiceAdmin = surveyServiceAdmin;
    }

    @PostMapping("/admin/addSurvey")
    public ResponseEntity<?> addSurvey(@Valid @RequestBody Survey survey){
        return surveyServiceAdmin.checkAndAddSurvey(survey);
    }


    @DeleteMapping("/admin/deleteSurvey")
    public ResponseEntity<?> deleteSurveyById(@RequestParam Integer surveyId){
        return surveyServiceAdmin.deleteSurveyById(surveyId);
    }

    @PutMapping("/admin/updateSurvey")
    public ResponseEntity<?> updateSurvey(@RequestBody Survey survey){
        return surveyServiceAdmin.checkAndUpdateSurvey(survey);
    }

    @PutMapping("/admin/publishSurvey")
    public ResponseEntity<?> publishSurvey(@RequestParam Integer surveyId){
        return surveyServiceAdmin.checkAndPublishSurvey(surveyId);
    }




}
