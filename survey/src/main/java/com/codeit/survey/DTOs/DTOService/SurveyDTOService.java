package com.codeit.survey.DTOs.DTOService;

import com.codeit.survey.DTOs.EntitiyListDTOs.SurveysDTO;
import com.codeit.survey.DTOs.EntityDTOs.SurveyDTO;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.services.QuestionService;
import com.codeit.survey.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SurveyDTOService {
    private UserService userService;
    private QuestionService questionService;

    @Autowired
    public SurveyDTOService(UserService userService, QuestionService questionService){
        this.questionService = questionService;
        this.userService = userService;
    }

    public SurveyDTO createDTOFromSurvey(Survey survey){
        return new SurveyDTO(
                survey.getId(),
                survey.getCreationDate(),
                survey.getName(),
                userService.createDTOFromSurveyUser(survey.getSurveyUser()),
                questionService.createDTOsFromQuestions(survey.getQuestions()),
                survey.isPublished()
        );
    }

    public SurveysDTO createDTOFromSurveys(List<Survey> surveys){
        SurveysDTO surveysDTO = new SurveysDTO(new ArrayList<>());

        for (Survey survey : surveys){
            surveysDTO.getSurveys().add(createDTOFromSurvey(survey));
        }
        return surveysDTO;
    }

}
