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
    private UserService userService;

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
    public QuestionService(QuestionRepo questionRepo, ChoiceService choiceService, SurveyService surveyService, UserService userService){
        this.questionRepo = questionRepo;
        this.choiceService = choiceService;
        this.surveyService = surveyService;
        this.userService = userService;
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

    /**
     * @return true if all the questions belong to surveys of the user
     */
    private boolean isUserSurvey(List<Question> questions, Authentication auth){
        SurveyUser surveyUser = userService.getSurveyUserByUserName
                (
                    ((CustomUserDetails)auth.getPrincipal()).
                    getUsername()
                );
        List<Integer> userSurveysIds =
                surveyService.
                        getSurveysByUserId(surveyUser.getId()).
                        stream().
                        map(Survey::getId).
                        collect(Collectors.toList());

        List<Question> questionsNotOfUser =  questions.
                stream()
                .filter(question -> !userSurveysIds.contains(question.getSurvey().getId()))
                .collect(Collectors.toList());

        return questionsNotOfUser.size() == 0;
    }

    public ResponseEntity<?> addQuestionsToSurvey(List<Question> questions){
        // do the surveys of the questions belong to the user
        if (! isUserSurvey(questions, SecurityContextHolder.getContext().getAuthentication())){
            return ResponseEntity.badRequest().build();
        }

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
