package com.codeit.survey.services.adminServices;

import com.codeit.survey.DTOs.DTOService.SurveyDTOService;
import com.codeit.survey.DTOs.EntityDTOs.SurveyDTO;
import com.codeit.survey.entities.Choice;
import com.codeit.survey.entities.Question;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.SurveyRepo;
import com.codeit.survey.services.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SurveyServiceAdmin {
    private SurveyRepo surveyRepo;
    private SurveyDTOService surveyDTOService;
    private VerificationService verificationService;

    @Autowired
    public SurveyServiceAdmin(SurveyRepo surveyRepo, SurveyDTOService surveyDTOService, VerificationService verificationService){
        this.surveyRepo = surveyRepo;
        this.surveyDTOService = surveyDTOService;
        this.verificationService = verificationService;
    }

    public Survey findById(Integer id){
        return surveyRepo.findById(id).orElse(null);
    }

    private ResponseEntity<?> updateSurvey(Survey newSurvey, Survey survey) {
        survey.setName(newSurvey.getName());
        survey.getQuestions().clear();
        survey.getQuestions().addAll(newSurvey.getQuestions());
        setQuestionsAndChoices(survey);

        surveyRepo.save(survey);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> publishSurvey(Survey surveyFromDB) {
        surveyFromDB.setPublished(true);
        surveyRepo.save(surveyFromDB);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> addSurvey(Survey survey) {
        survey.setCreationDate(java.time.LocalDateTime.now());
        setQuestionsAndChoices(survey);
        surveyRepo.save(survey);
        return ResponseEntity.ok().build();
    }

    private void setQuestionsAndChoices(Survey survey){
        // set the survey of the question
        for (Question question : survey.getQuestions()){
            question.setSurvey(survey);
            // set the question of the choice
            for (Choice choice : question.getChoices()){
                choice.setQuestion(question);
            }
        }
    }

    private boolean surveyNameNotUniquePerUser(Survey survey) {
        SurveyUser surveyUser = verificationService.getAuthenticatedSurveyUser();
        return surveyRepo.existsByNameAndSurveyUser(survey.getName(), surveyUser);
    }


    public ResponseEntity<?> deleteSurveyById(Integer surveyId){
        Survey survey = surveyRepo.findById(surveyId).orElse(null);
        if (survey == null){
            return ResponseEntity.badRequest().build();
        }
        surveyRepo.delete(survey);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> checkAndUpdateSurvey(Survey newSurvey){
        Survey survey = surveyRepo.findById(newSurvey.getId()).orElse(null);
        if (survey == null){
            return ResponseEntity.badRequest().body("Survey doesn't exist");
        }
        if(survey.isPublished()){
            return ResponseEntity.badRequest().body("Survey can't be updated because it's published");
        }
        if (userHasAnotherSurveyWithSameName(newSurvey, survey)){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return updateSurvey(newSurvey, survey);
    }

    private boolean userHasAnotherSurveyWithSameName(Survey newSurvey, Survey survey) {
        return !newSurvey.getName().equalsIgnoreCase(survey.getName()) && surveyNameNotUniquePerUser(newSurvey);
    }

    public ResponseEntity<?> checkAndPublishSurvey(Integer surveyId){
        Survey surveyFromDB = findById(surveyId);
        if(surveyFromDB == null){
            return ResponseEntity.badRequest().body("No such Survey");
        }

        if(surveyFromDB.isPublished()){
            return ResponseEntity.badRequest().body("Survey already published");
        }

        return publishSurvey(surveyFromDB);
    }

    public ResponseEntity<?> checkAndAddSurvey(Survey survey){
        if (surveyNameNotUniquePerUser(survey)){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return addSurvey(survey);
    }

    public ResponseEntity<?> getSurveyById(Integer id){
        Optional<Survey> survey = surveyRepo.findById(id);
        if(survey.isPresent()){
            SurveyDTO surveyDTO = surveyDTOService.createDTOFromSurvey(survey.get());
            return ResponseEntity.ok().body(surveyDTO);
        }
        else{
            return ResponseEntity.badRequest().body("No such Survey exists");
        }
    }

}
