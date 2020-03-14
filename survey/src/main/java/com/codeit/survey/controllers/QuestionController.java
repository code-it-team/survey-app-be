package com.codeit.survey.controllers;

import com.codeit.survey.entities.Question;
import com.codeit.survey.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {
    private QuestionService questionService;

    @Autowired
    public  QuestionController(QuestionService questionService){
        this.questionService = questionService;
    }

    @PostMapping("/SurveyAddQuestion")
    public ResponseEntity<?> addQuestionToSurvey(@RequestBody Question question){
        return questionService.addQuestionToSurvey(question);
    }
}
