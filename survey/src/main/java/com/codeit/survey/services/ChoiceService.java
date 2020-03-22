package com.codeit.survey.services;

import com.codeit.survey.DTOs.EntityDTOs.ChoiceDTO;
import com.codeit.survey.entities.Choice;
import com.codeit.survey.repositories.ChoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChoiceService {
    private ChoiceRepo choiceRepo;

    @Autowired
    public ChoiceService(ChoiceRepo choiceRepo){
        this.choiceRepo = choiceRepo;
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

}
