package com.codeit.survey.repositories;

import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SurveyRepo extends CrudRepository<Survey, Integer> {
    List<Survey> findSurveysBySurveyUser(SurveyUser surveyUser);

    @Query("select case when count(s)> 0 then true else false end from Survey s where s.name=:name and s.surveyUser=:surveyUser")
    boolean existsByNameAndSurveyUser(@Param("name")String name, @Param("surveyUser") SurveyUser surveyUser);

}
