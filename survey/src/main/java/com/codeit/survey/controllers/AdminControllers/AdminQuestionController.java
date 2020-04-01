package com.codeit.survey.controllers.AdminControllers;

import com.codeit.survey.entities.Question;
import com.codeit.survey.services.AdminServices.AdminQuestionService;
import com.codeit.survey.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminQuestionController {

    private AdminQuestionService questionService;

    @Autowired
    public  AdminQuestionController(AdminQuestionService questionService){
        this.questionService = questionService;
    }

    @PostMapping("admin/SurveyAddQuestions")
    public ResponseEntity<?> addQuestionsToSurvey(@RequestBody List<Question> questions){
        return questionService.addQuestionsToSurvey(questions);
    }

    @DeleteMapping("admin/SurveyDeleteQuestions")
    public ResponseEntity<?> deleteQuestionsFromSurvey(@RequestBody List<Question> questions){
        return questionService.deleteQuestionsFromSurvey(questions);
    }

    @PutMapping("admin/SurveyUpdateQuestions")
    public ResponseEntity<?> updateSurveyQuestions(@RequestBody List<Question> questions){
        return questionService.updateSurveyQuestion(questions);
    }



}
