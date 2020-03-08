package com.codeit.survey.builders;


import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;

import java.util.Date;

public class SurveyBuilder {
    private SurveyUser surveyUser;
    private Date creationDate;
    private String name;


    public SurveyBuilder setSurveyUser(SurveyUser surveyUser){
        this.surveyUser = surveyUser;
        return this;
    }

    public SurveyBuilder setCreationDate(Date creationDate){
        this.creationDate = creationDate;
        return this;
    }

    public SurveyBuilder setName(String name){
        this.name = name;
        return this;
    }

    public Survey buildSurvey(){
        return new Survey(null, surveyUser, creationDate, name);
    }

}
