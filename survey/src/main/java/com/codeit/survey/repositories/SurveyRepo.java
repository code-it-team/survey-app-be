package com.codeit.survey.repositories;

import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SurveyRepo extends CrudRepository<Survey, Integer> {
    Survey findSurveyBySurveyUser(SurveyUser surveyUser);

}
