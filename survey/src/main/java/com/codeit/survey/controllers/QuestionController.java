package com.codeit.survey.controllers;

import com.codeit.survey.controllers.validationInterface.QuestionCreation;
import com.codeit.survey.entities.Question;
import com.codeit.survey.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class QuestionController {
    private QuestionService questionService;

    @Autowired
    public  QuestionController(QuestionService questionService){
        this.questionService = questionService;
    }

    @PostMapping("/SurveyAddQuestion")
    public ResponseEntity<?> addQuestionToSurvey(@Validated(QuestionCreation.class) @RequestBody Question question){
        return questionService.addQuestionToSurvey(question);
    }

    @DeleteMapping("/SurveyDeleteQuestion")
    public ResponseEntity<?> deleteQuestionsFromSurvey(@RequestParam Integer questionId){
        return questionService.deleteQuestionsFromSurvey(questionId);
    }

    @PutMapping("/SurveyUpdateQuestion")
    public ResponseEntity<?> updateSurveyQuestions(@RequestBody Question question){
        return questionService.updateSurveyQuestion(question);
    }


}
