package com.codeit.survey.controllers;

import com.codeit.survey.DTOs.SurveySubmissionDTO;
import com.codeit.survey.services.SurveySubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SurveySubmissionController {
    private SurveySubmissionService surveySubmissionService;

    @Autowired
    public SurveySubmissionController(SurveySubmissionService surveySubmissionService) {
        this.surveySubmissionService = surveySubmissionService;
    }

    @PostMapping("/submitSurvey")
    public ResponseEntity<?> submitSurvey(@RequestBody @Validated SurveySubmissionDTO surveySubmissionDTO){
        return surveySubmissionService.checkSurveyAndSubmit(surveySubmissionDTO);
    }



}
