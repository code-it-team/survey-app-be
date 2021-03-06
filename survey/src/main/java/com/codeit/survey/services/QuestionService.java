package com.codeit.survey.services;

import com.codeit.survey.DTOs.EntityDTOs.QuestionDTO;
import com.codeit.survey.aspects.annotations.VerifySurveyBelongToUser;
import com.codeit.survey.entities.Choice;
import com.codeit.survey.entities.Question;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.repositories.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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

    private void setSurvey(Question question){
        Integer SurveyId = question.getSurvey().getId();
        Survey survey = surveyService.findById(SurveyId);
        // set the survey question relationship
        survey.getQuestions().add(question);
        question.setSurvey(survey);
    }

    private void setChoices(Question question){
        for (Choice choice : question.getChoices()){
            choice.setQuestion(question);
        }
    }

    private void  updateQuestion(Question newQuestion){
        Integer newQuestionId = newQuestion.getId();
        Question question = questionRepo.findById(newQuestionId).orElseThrow(EntityNotFoundException::new);
        if(newQuestion.getBody() != null){
            question.setBody(newQuestion.getBody());
        }
        questionRepo.save(question);
    }


    // admin methods
    public ResponseEntity<?> addQuestionToSurvey_admin(Question question){
        try{
            setSurvey(question);
            setChoices(question);

            questionRepo.save(question);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> deleteQuestionFromSurvey_admin(Integer questionId){
        try {
            Question question = questionRepo.findById(questionId).orElse(null);
            if (question == null){
                return ResponseEntity.badRequest().build();
            }
            questionRepo.deleteById(questionId);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> updateSurveyQuestion_admin(Question question){
        try {
            updateQuestion(question);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }


    // user methods
    @VerifySurveyBelongToUser
    public ResponseEntity<?> addQuestionToSurvey(Question question){
        return addQuestionToSurvey_admin(question);
    }

    public ResponseEntity<?> deleteQuestionsFromSurvey(Integer questionId){
        return deleteQuestionFromSurvey_admin(questionId);
    }

    @VerifySurveyBelongToUser
    public ResponseEntity<?> updateSurveyQuestion(Question question){
        return updateSurveyQuestion_admin(question);
    }

}
