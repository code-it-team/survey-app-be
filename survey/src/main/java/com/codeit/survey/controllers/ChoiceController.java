package com.codeit.survey.controllers;

import com.codeit.survey.entities.Choice;
import com.codeit.survey.services.ChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChoiceController {
    private ChoiceService choiceService;

    @Autowired
    public ChoiceController(ChoiceService choiceService){
        this.choiceService = choiceService;
    }

    @PostMapping("/addChoice")
    public ResponseEntity<?> addChoice(@RequestBody Choice choice){
        return choiceService.addChoice(choice);
    }

    @DeleteMapping("/deleteChoice")
    public ResponseEntity<?> deleteChoice(@RequestParam Integer choiceId){
        return choiceService.deleteChoice(choiceId);
    }
}
