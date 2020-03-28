package com.codeit.survey.services;

import com.codeit.survey.DTOs.EntityDTOs.SurveyDTO;
import com.codeit.survey.DTOs.EntitiyListDTOs.SurveysDTO;
import com.codeit.survey.entities.Choice;
import com.codeit.survey.entities.Question;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.SurveyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SurveyService {
    private SurveyRepo surveyRepo;
    private UserService userService;
    private QuestionService questionService;


    @Autowired
    public SurveyService(SurveyRepo surveyRepo, UserService userService, QuestionService questionService){
        this.surveyRepo = surveyRepo;
        this.userService = userService;
        this.questionService = questionService;
    }

    private SurveysDTO createDTOFromSurveys(List<Survey> surveys){
        SurveysDTO surveysDTO = new SurveysDTO(new ArrayList<>());

        for (Survey survey : surveys){

            surveysDTO.getSurveyDTOS().add(new SurveyDTO(
                    survey.getId(),
                    survey.getCreationDate(),
                    survey.getName(),
                    userService.createDTOFromSurveyUser(survey.getSurveyUser()),
                    questionService.createDTOsFromQuestions(survey.getQuestions())
            ));
        }
        return surveysDTO;
    }

    private void setQuestionsAndChoices(Survey survey){
        // set the survey of the question
        for (Question question : survey.getQuestions()){
            question.setSurvey(survey);
            // set the question of the choice
            for (Choice choice : question.getChoices()){
                choice.setQuestion(question);
            }
        }
    }


    public ResponseEntity<?> addSurvey(Survey survey){
        try{

            survey.setCreationDate(java.time.LocalDateTime.now());
            setQuestionsAndChoices(survey);
            surveyRepo.save(survey);
            return ResponseEntity.ok().build();

        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> getSurveysByUser(SurveyUser surveyUser){
        List<Survey> surveys = surveyRepo.findSurveysBySurveyUser(surveyUser);
        return ResponseEntity.ok(createDTOFromSurveys(surveys));
    }

    public ResponseEntity<?> getSurveysByUserId(Integer userId){
        SurveyUser surveyUser = userService.getUserById(userId);
        if(surveyUser == null){
            return ResponseEntity.badRequest().build();
        }
        List<Survey> surveys = surveyRepo.findSurveysBySurveyUser(surveyUser);
        return ResponseEntity.ok(createDTOFromSurveys(surveys));
    }

}
