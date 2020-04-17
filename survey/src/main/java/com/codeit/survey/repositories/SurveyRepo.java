package com.codeit.survey.repositories;

import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SurveyRepo extends CrudRepository<Survey, Integer> {
    List<Survey> findSurveysBySurveyUser(SurveyUser surveyUser);
    boolean existsByName(String name);

}
