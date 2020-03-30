package com.codeit.survey.controllers;

import com.codeit.survey.entities.Question;
import com.codeit.survey.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionController {
    private QuestionService questionService;

    @Autowired
    public  QuestionController(QuestionService questionService){
        this.questionService = questionService;
    }

    @PostMapping("/SurveyAddQuestions")
    public ResponseEntity<?> addQuestionsToSurvey(@RequestBody List<Question> questions){
        return questionService.addQuestionsToSurvey(questions);
    }

    @DeleteMapping("/SurveyDeleteQuestions")
    public ResponseEntity<?> deleteQuestionsFromSurvey(@RequestBody List<Question> questions){
        return questionService.deleteQuestionsFromSurvey(questions);
    }

    @PutMapping("/SurveyUpdateQuestions")
    public ResponseEntity<?> updateSurveyQuestions(@RequestBody List<Question> questions){
        return questionService.updateSurveyQuestion(questions);
    }


}
