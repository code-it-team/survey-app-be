package com.codeit.survey.repositories;

import com.codeit.survey.entities.Choice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoiceRepo extends CrudRepository<Choice, Integer> {
}
