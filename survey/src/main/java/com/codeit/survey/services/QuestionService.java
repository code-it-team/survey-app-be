package com.codeit.survey.services;

import com.codeit.survey.DTOs.EntityDTOs.QuestionDTO;
import com.codeit.survey.entities.Question;
import com.codeit.survey.repositories.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private QuestionRepo questionRepo;
    private ChoiceService choiceService;

    public List<QuestionDTO> createDTOsFromQuestions(List<Question> questions){
        return questions
                .stream()
                .map(question -> new QuestionDTO(
                        question.getId(),
                        question.getBody(),
                        choiceService.createDTOsFromChoices(question.getChoices())))
                .collect(Collectors.toList());
    }

    @Autowired
    public QuestionService(QuestionRepo questionRepo, ChoiceService choiceService){
        this.questionRepo = questionRepo;
        this.choiceService = choiceService;
    }

    public ResponseEntity<?> addQuestionToSurvey(Question question){
        try{
            questionRepo.save(question);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

}
