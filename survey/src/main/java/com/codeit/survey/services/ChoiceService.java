package com.codeit.survey.services;

import com.codeit.survey.repositories.ChoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChoiceService {
    private ChoiceRepo choiceRepo;

    @Autowired
    public ChoiceService(ChoiceRepo choiceRepo){
        this.choiceRepo = choiceRepo;
    }

}
