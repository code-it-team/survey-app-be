package com.codeit.survey.services;

import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyPublication;
import com.codeit.survey.repositories.SurveyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyPublicationService {
    private SurveyRepo surveyRepo;

    @Autowired
    SurveyPublicationService(SurveyRepo surveyRepo){
        this.surveyRepo = surveyRepo;
    }

    public void publishSurvey(Survey survey, String clientURL){
        String surveyPublicationURL = generateSurveyPublicationURL(survey.getId() ,clientURL);

        SurveyPublication surveyPublication = new SurveyPublication(surveyPublicationURL, java.time.LocalDateTime.now(), survey);

        survey.setSurveyPublication(surveyPublication);

        surveyRepo.save(survey);
    }

    private String generateSurveyPublicationURL(Integer id, String clientURL) {
        String rootClientURL = clientURL.substring(0, clientURL.lastIndexOf("/"));
        return rootClientURL + "/submitSurvey/" + id;
    }
}
