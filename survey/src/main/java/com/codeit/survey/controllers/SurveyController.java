package com.codeit.survey.controllers;

import com.codeit.survey.controllers.validationInterface.SurveyCreation;
import com.codeit.survey.controllers.validationInterface.SurveyUpdate;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SurveyController {
    private SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService){
        this.surveyService = surveyService;
    }

    @PostMapping("/addSurvey")
    public ResponseEntity<?> addSurvey(@Validated(SurveyCreation.class) @RequestBody Survey survey){
        return surveyService.checkAndAddSurvey(survey);
    }

    @GetMapping("/getSurveysByUser")
    public ResponseEntity<?> getSurveysByUser(@RequestParam("id") Integer id){
        return surveyService.getSurveysByUserId_response(id);
    }

    @GetMapping("/survey")
    public ResponseEntity<?> getSurveyById(@RequestParam Integer surveyId){
        return surveyService.getSurveyById(surveyId);
    }

    @DeleteMapping("/deleteSurvey")
    public ResponseEntity<?> deleteSurveyById(@RequestParam Integer surveyId){
        return surveyService.deleteSurveyById(surveyId);
    }

    @PutMapping("/updateSurvey")
    public ResponseEntity<?> updateSurvey(@Validated({SurveyUpdate.class}) @RequestBody Survey survey){
        return surveyService.checkAndUpdateSurvey(survey);
    }

    @PutMapping("/publishSurvey")
    public ResponseEntity<?> publishSurvey(@RequestParam Integer surveyId, HttpServletRequest request){
        return surveyService.checkAndPublishSurvey(surveyId, request.getRequestURL().toString());
    }

    @GetMapping("/publicationLink")
    public ResponseEntity<?> getSurveyPublicationLink(@RequestParam Integer surveyId){
        return surveyService.getSurveyPublicationLink(surveyId);
    }



}
