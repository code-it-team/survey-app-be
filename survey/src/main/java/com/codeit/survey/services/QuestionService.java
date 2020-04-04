package com.codeit.survey.services;

import com.codeit.survey.DTOs.EntityDTOs.QuestionDTO;
import com.codeit.survey.entities.Choice;
import com.codeit.survey.entities.Question;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.QuestionRepo;
import com.codeit.survey.security.CustomUserDetails;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private QuestionRepo questionRepo;
    private ChoiceService choiceService;
    private SurveyService surveyService;
    private VerificationService verificationService;

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
    public QuestionService(QuestionRepo questionRepo, ChoiceService choiceService, SurveyService surveyService, VerificationService verificationService){
        this.questionRepo = questionRepo;
        this.choiceService = choiceService;
        this.surveyService = surveyService;
        this.verificationService = verificationService;
    }

    private void setSurvey(Question question){
        Survey survey = surveyService.findById(question.getSurvey().getId());
        // set the survey question relationship
        survey.getQuestions().add(question);
        question.setSurvey(survey);
    }

    private void setChoices(Question question){
        for (Choice choice : question.getChoices()){
            choice.setQuestion(question);
        }
    }
    /**
     * question must have its ID set
     * */
    private void updateQuestion(Question newQuestion){
        Question question = questionRepo.findById(newQuestion.getId()).orElseThrow(RuntimeException::new);
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
    public ResponseEntity<?> addQuestionToSurvey(Question question){
        // do the surveys of the questions belong to the user
        if ( verificationService.notUserSurvey(question.getSurvey().getId()))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // if the user has proven that he owns all the questions, he can add as an admin
        return addQuestionToSurvey_admin(question);
    }

    public ResponseEntity<?> deleteQuestionsFromSurvey(Integer questionId){
        Question question = questionRepo.findById(questionId).orElse(null);
        if (question == null){
            return ResponseEntity.badRequest().build();
        }
        // do the surveys of the questions belong to the user
        if (verificationService.notUserSurvey(question.getSurvey().getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return deleteQuestionFromSurvey_admin(questionId);
    }

    public ResponseEntity<?> updateSurveyQuestion(Question question){
        // do the surveys of the questions belong to the user
        if (verificationService.notUserSurvey(question.getSurvey().getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return updateSurveyQuestion_admin(question);
    }

}
