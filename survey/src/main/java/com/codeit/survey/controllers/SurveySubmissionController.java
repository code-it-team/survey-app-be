package com.codeit.survey.controllers;

import com.codeit.survey.DTOs.SurveySubmissionDTO;
import com.codeit.survey.services.SurveyStatisticsService;
import com.codeit.survey.services.SurveySubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class SurveySubmissionController {
    private SurveySubmissionService surveySubmissionService;
    private SurveyStatisticsService surveyStatisticsService;

    @Autowired
    public SurveySubmissionController(SurveySubmissionService surveySubmissionService, SurveyStatisticsService surveyStatisticsService) {
        this.surveySubmissionService = surveySubmissionService;
        this.surveyStatisticsService = surveyStatisticsService;
    }

    @PostMapping("/submitSurvey")
    public ResponseEntity<?> submitSurvey(@RequestBody @Validated SurveySubmissionDTO surveySubmissionDTO){
        return surveySubmissionService.checkSurveyAndSubmit(surveySubmissionDTO);
    }

    @GetMapping("/surveyStatistics")
    public ResponseEntity<?> getSurveyStatistics(@RequestParam Integer surveyId){
        return surveyStatisticsService.getSurveyStatistics(surveyId);
    }



}
