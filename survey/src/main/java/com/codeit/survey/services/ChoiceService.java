package com.codeit.survey.services;

import com.codeit.survey.DTOs.EntityDTOs.ChoiceDTO;
import com.codeit.survey.entities.Choice;
import com.codeit.survey.repositories.ChoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChoiceService {
    private ChoiceRepo choiceRepo;
    private VerificationService verificationService;

    @Autowired
    public ChoiceService(ChoiceRepo choiceRepo, VerificationService verificationService){
        this.choiceRepo = choiceRepo;
        this.verificationService = verificationService;
    }

    public List<ChoiceDTO> createDTOsFromChoices(List<Choice> choices){
        return choices
                .stream()
                .map(choice -> new ChoiceDTO(choice.getId(), choice.getBody()))
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> addChoice_admin(Choice choice){
        try{
            choiceRepo.save(choice);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> deleteChoice_admin(Integer choiceId){
        Choice choice = choiceRepo.findById(choiceId).orElse(null);
        if (choice == null) return ResponseEntity.badRequest().build();
        choiceRepo.delete(choice);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> updateChoice_admin(Choice newChoice){
        Choice choice = choiceRepo.findById(newChoice.getId()).orElse(null);
        if (choice == null) return ResponseEntity.badRequest().build();

        // update the choice
        choice.setBody(newChoice.getBody());
        choiceRepo.save(choice);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> addChoice(Choice choice){
        Integer surveyId = choice.getQuestion().getSurvey().getId();
        if(verificationService.notUserSurvey(surveyId)) return ResponseEntity.badRequest().build();
        return addChoice_admin(choice);
    }

    public ResponseEntity<?> deleteChoice(Integer choiceId){
        Choice choice = choiceRepo.findById(choiceId).orElse(null);
        if (choice == null) return ResponseEntity.badRequest().build();
        if (verificationService.notUserSurvey(
        choice.getQuestion().getSurvey().getId())){
            return ResponseEntity.badRequest().build();
        }
        return deleteChoice_admin(choiceId);
    }

    public ResponseEntity<?> updateChoice(Choice newChoice){
        Integer surveyId = newChoice.getQuestion().getSurvey().getId();
        if (verificationService.notUserSurvey(surveyId)) return ResponseEntity.badRequest().build();
        return updateChoice_admin(newChoice);
    }


}
