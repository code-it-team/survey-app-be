package com.codeit.survey.services;

import com.codeit.survey.DTOs.EntityDTOs.QuestionDTO;
import com.codeit.survey.entities.Choice;
import com.codeit.survey.entities.Question;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.repositories.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private QuestionRepo questionRepo;
    private ChoiceService choiceService;
    private SurveyService surveyService;

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
    @Lazy
    public QuestionService(QuestionRepo questionRepo, ChoiceService choiceService, SurveyService surveyService){
        this.questionRepo = questionRepo;
        this.choiceService = choiceService;
        this.surveyService = surveyService;
    }

    private void setSurvey(List<Question> questions){
        for (Question question : questions){
            Survey survey = surveyService.findById(question.getSurvey().getId());
            // set the survey question relationship
            survey.getQuestions().add(question);
            question.setSurvey(survey);
        }
    }

    private void setChoices(List<Question> questions){
        for (Question question : questions){
            for (Choice choice : question.getChoices()){
                choice.setQuestion(question);
            }
        }
    }

    public ResponseEntity<?> addQuestionsToSurvey(List<Question> questions){
        try{
            setSurvey(questions);
            setChoices(questions);

            questionRepo.saveAll(questions);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> deleteQuestionsFromSurvey(List<Question> questions){
        try {
            questionRepo.deleteAll(questions);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * questions in newQuestions list must have their ID set
     * */
    private void updateQuestions(List<Question> newQuestions){
        for (Question newQuestion : newQuestions){
            Question question = questionRepo.findById(newQuestion.getId()).orElseThrow(RuntimeException::new);
            if(newQuestion.getBody() != null){
                question.setBody(newQuestion.getBody());
            }
            questionRepo.save(question);
        }
    }

    public ResponseEntity<?> updateSurveyQuestion(List<Question> questions){
        try {
            updateQuestions(questions);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

}
