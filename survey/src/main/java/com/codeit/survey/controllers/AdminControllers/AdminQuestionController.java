package com.codeit.survey.controllers.AdminControllers;

import com.codeit.survey.entities.Question;
import com.codeit.survey.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminQuestionController {

    private QuestionService questionService;

    @Autowired
    public  AdminQuestionController(QuestionService questionService){
        this.questionService = questionService;
    }

    @PostMapping("admin/SurveyAddQuestion")
    public ResponseEntity<?> addQuestionsToSurvey(@RequestBody Question question){
        return questionService.addQuestionToSurvey_admin(question);
    }

    @DeleteMapping("admin/SurveyDeleteQuestion")
    public ResponseEntity<?> deleteQuestionsFromSurvey(@RequestParam Integer questionId){
        return questionService.deleteQuestionFromSurvey_admin(questionId);
    }

    @PutMapping("admin/SurveyUpdateQuestion")
    public ResponseEntity<?> updateSurveyQuestions(@RequestBody Question question){
        return questionService.updateSurveyQuestion_admin(question);
    }



}
