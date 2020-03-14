package com.codeit.survey.repositories;

import com.codeit.survey.entities.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepo extends CrudRepository<Question, Integer> {
}
