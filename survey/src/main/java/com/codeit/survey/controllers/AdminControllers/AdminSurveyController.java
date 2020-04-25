package com.codeit.survey.controllers.AdminControllers;

import com.codeit.survey.controllers.validationInterface.SurveyCreation;
import com.codeit.survey.controllers.validationInterface.SurveyUpdate;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.services.adminServices.SurveyServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminSurveyController {
    private SurveyServiceAdmin surveyServiceAdmin;

    @Autowired
    public AdminSurveyController(SurveyServiceAdmin surveyServiceAdmin){
        this.surveyServiceAdmin = surveyServiceAdmin;
    }

    @PostMapping("/admin/addSurvey")
    public ResponseEntity<?> addSurvey(@Validated(SurveyCreation.class) @RequestBody Survey survey){
        return surveyServiceAdmin.checkAndAddSurvey(survey);
    }


    @DeleteMapping("/admin/deleteSurvey")
    public ResponseEntity<?> deleteSurveyById(@RequestParam Integer surveyId){
        return surveyServiceAdmin.deleteSurveyById(surveyId);
    }

    @PutMapping("/admin/updateSurvey")
    public ResponseEntity<?> updateSurvey(@Validated({SurveyUpdate.class}) @RequestBody Survey survey){
        return surveyServiceAdmin.checkAndUpdateSurvey(survey);
    }

    @PutMapping("/admin/publishSurvey")
    public ResponseEntity<?> publishSurvey(@RequestParam Integer surveyId){
        return surveyServiceAdmin.checkAndPublishSurvey(surveyId);
    }

    @GetMapping("/admin/survey")
    public ResponseEntity<?> getSurveyById(@RequestParam Integer surveyId){
        return surveyServiceAdmin.getSurveyById(surveyId);
    }




}
