package com.codeit.survey.repositories;

import com.codeit.survey.entities.SurveyUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends CrudRepository<SurveyUser, Integer> {
    Optional<SurveyUser> findByUsername(String userName);
    List<SurveyUser> findAll();
}