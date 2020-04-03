package com.codeit.survey.controllers;

import com.codeit.survey.DTOs.EntityDTOs.SurveyDTO;
import com.codeit.survey.DTOs.EntityDTOs.SurveyUserDTO;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SurveyController {
    private SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService){
        this.surveyService = surveyService;
    }

    @PostMapping("/addSurvey")
    public ResponseEntity<?> addSurvey(@RequestBody Survey survey){
        return surveyService.addSurvey(survey);
    }

    @GetMapping("/getSurveysByUser")
    public ResponseEntity<?> getSurveysByUser(@RequestParam("id") Integer id){
        return surveyService.getSurveysByUserId_response(id);
    }



}
