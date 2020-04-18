package com.codeit.survey.builders;


import com.codeit.survey.entities.Question;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;

import java.time.LocalDateTime;
import java.util.List;

public class SurveyBuilder {
    private SurveyUser surveyUser;
    private LocalDateTime creationDate;
    private String name;
    private List<Question> questions;


    public SurveyBuilder setSurveyQuestions(List<Question> questions){
        this.questions = questions;
        return this;
    }

    public SurveyBuilder setSurveyUser(SurveyUser surveyUser){
        this.surveyUser = surveyUser;
        return this;
    }

    public SurveyBuilder setCreationDate(LocalDateTime creationDate){
        this.creationDate = creationDate;
        return this;
    }

    public SurveyBuilder setName(String name){
        this.name = name;
        return this;
    }

    public Survey buildSurvey(){
        return new Survey(null, false,surveyUser, creationDate, name, questions);
    }

}
