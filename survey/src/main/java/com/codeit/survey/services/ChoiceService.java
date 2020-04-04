package com.codeit.survey.services;

import com.codeit.survey.DTOs.EntityDTOs.ChoiceDTO;
import com.codeit.survey.entities.Choice;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.ChoiceRepo;
import com.codeit.survey.security.CustomUserDetails;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChoiceService {
    private ChoiceRepo choiceRepo;
    private UserService userService;
    private SurveyService surveyService;
    private VerificationService verificationService;

    @Autowired
    public ChoiceService(ChoiceRepo choiceRepo, UserService userService, SurveyService surveyService, VerificationService verificationService){
        this.choiceRepo = choiceRepo;
        this.userService = userService;
        this.surveyService = surveyService;
        this.verificationService = verificationService;
    }

    public Choice getChoiceById(Integer id){
        return choiceRepo.findById(id).orElse(null);
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

    public ResponseEntity<?> addChoice(Choice choice){
        if(verificationService.notUserSurvey(choice.getQuestion().getSurvey().getId())) return ResponseEntity.badRequest().build();
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

}
