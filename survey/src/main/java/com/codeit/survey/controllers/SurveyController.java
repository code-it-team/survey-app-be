package com.codeit.survey.controllers;

import com.codeit.survey.entities.Survey;
import com.codeit.survey.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class SurveyController {
    private SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService){
        this.surveyService = surveyService;
    }

    @PostMapping("/addSurvey")
    public ResponseEntity<?> addSurvey(@Valid @RequestBody Survey survey){
        return surveyService.checkAndAddSurvey(survey);
    }

    @GetMapping("/getSurveysByUser")
    public ResponseEntity<?> getSurveysByUser(@RequestParam("id") Integer id){
        return surveyService.getSurveysByUserId_response(id);
    }

    @DeleteMapping("/deleteSurvey")
    public ResponseEntity<?> deleteSurveyById(@RequestParam Integer surveyId){
        return surveyService.deleteSurveyById(surveyId);
    }

    @PutMapping("/updateSurvey")
    public ResponseEntity<?> updateSurvey(@RequestBody Survey survey){
        return surveyService.checkAndUpdateSurvey(survey);
    }

    @PutMapping("/publishSurvey")
    public ResponseEntity<?> publishSurvey(@RequestParam Integer surveyId){
        return surveyService.checkAndPublishSurvey(surveyId);
    }



}
