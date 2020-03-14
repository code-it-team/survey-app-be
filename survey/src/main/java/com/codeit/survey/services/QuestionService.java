package com.codeit.survey.services;

import com.codeit.survey.entities.Question;
import com.codeit.survey.repositories.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    private QuestionRepo questionRepo;

    @Autowired
    public QuestionService(QuestionRepo questionRepo){
        this.questionRepo = questionRepo;
    }

    public ResponseEntity<?> addQuestionToSurvey(Question question){
        try{
            questionRepo.save(question);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

}
