package com.codeit.survey.controllers;

import com.codeit.survey.services.ChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChoiceController {
    private ChoiceService choiceService;

    @Autowired
    public ChoiceController(ChoiceService choiceService){
        this.choiceService = choiceService;
    }
}
