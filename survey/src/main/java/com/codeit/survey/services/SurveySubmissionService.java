package com.codeit.survey.services;

import com.codeit.survey.DTOs.QuestionSubmissionDTO;
import com.codeit.survey.DTOs.SurveySubmissionDTO;
import com.codeit.survey.entities.*;
import com.codeit.survey.repositories.SurveyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveySubmissionService {
    private SurveyService surveyService;
    private SurveyRepo surveyRepo;

    @Autowired
    public SurveySubmissionService(SurveyService surveyService, SurveyRepo surveyRepo) {
        this.surveyService = surveyService;
        this.surveyRepo = surveyRepo;
    }

    public ResponseEntity<?> checkSurveyAndSubmit(SurveySubmissionDTO surveySubmissionDTO){
        Survey survey = surveyService.findById(surveySubmissionDTO.getSurveyId());
        if(survey == null){
            String responseMessage = String.format("The Survey %s doesn't exist", surveySubmissionDTO.getSurveyId());
            return ResponseEntity.badRequest().body(responseMessage);
        }

        if(!survey.isPublished()){
            String responseMessage = String.format("The survey %s isn't published", survey.getId());
            return ResponseEntity.badRequest().body(responseMessage);
        }

        return checkQuestionsAndSubmit(surveySubmissionDTO, survey);
    }


    private ResponseEntity<?> checkQuestionsAndSubmit(SurveySubmissionDTO surveySubmissionDTO, Survey surveyFromDB){
        List<Integer> questionIds = getSurveyQuestionsIds(surveyFromDB);
        int countOfQuestionsSubmitted = surveySubmissionDTO.getQuestionSubmissions().size();

        if(questionIds.size() != countOfQuestionsSubmitted){
            return ResponseEntity.badRequest().body(String.format("The number of questions submitted (%s) isn't correct", countOfQuestionsSubmitted));
        }

         boolean nonMatchingQuestionIdsExists = nonMatchingQuestionIdsExists(surveySubmissionDTO, questionIds);

        if (nonMatchingQuestionIdsExists){
            return ResponseEntity.badRequest().body("Some Question Ids aren't correct");
        }
        return checkChoicesAndSubmit(surveySubmissionDTO, surveyFromDB);
    }

    private boolean nonMatchingQuestionIdsExists(SurveySubmissionDTO surveySubmissionDTO, List<Integer> questionIds) {
        return surveySubmissionDTO
               .getQuestionSubmissions()
               .stream()
               .map(QuestionSubmissionDTO::getQuestionId)
               .anyMatch(questionId -> !questionIds.contains(questionId));
    }

    private ResponseEntity<?> checkChoicesAndSubmit(SurveySubmissionDTO surveySubmissionDTO, Survey surveyFromDB) {
        List<Integer> choicesIdsFromDB = getChoicesIds(surveyFromDB);

        boolean nonMatchingChoicesIdsExists = nonMatchingChoicesIdsExists(surveySubmissionDTO, choicesIdsFromDB);

        if(nonMatchingChoicesIdsExists){
            return ResponseEntity.badRequest().body("Some Choices Ids aren't correct");
        }
        return submitSurvey(surveySubmissionDTO, surveyFromDB);
    }

    private ResponseEntity<?> submitSurvey(SurveySubmissionDTO surveySubmissionDTO, Survey surveyFromDB) {
        createNewSurveySubmission(surveySubmissionDTO, surveyFromDB);

        surveyRepo.save(surveyFromDB);

        return ResponseEntity.ok().build();
    }

    private void createNewSurveySubmission(SurveySubmissionDTO surveySubmissionDTO, Survey surveyFromDB) {

        SurveySubmission surveySubmission = new SurveySubmission();
        surveySubmission.setSubmissionDate(LocalDateTime.now());

        List<QuestionSubmission> questionsSubmissions =  createQuestionsSubmissions( surveySubmissionDTO, surveyFromDB);

        setSurveyPublicationToSurveySubmission(surveyFromDB, surveySubmission, questionsSubmissions);
    }

    private void setSurveyPublicationToSurveySubmission(Survey surveyFromDB, SurveySubmission surveySubmission, List<QuestionSubmission> questionsSubmissions) {
        setSurveySubmissionToQuestionSubmissions(surveySubmission, questionsSubmissions);

        SurveyPublication surveyPublication = surveyFromDB.getSurveyPublication();

        surveySubmission.setSurveyPublication(surveyPublication);

        surveyPublication.getSurveySubmissionList().add(surveySubmission);
    }

    private void setSurveySubmissionToQuestionSubmissions(SurveySubmission surveySubmission, List<QuestionSubmission> questionsSubmissions) {
        for(QuestionSubmission questionSubmission : questionsSubmissions){
            surveySubmission.getQuestionSubmissions().add(questionSubmission);
            questionSubmission.setSurveySubmission(surveySubmission);
        }
    }

    private List<QuestionSubmission> createQuestionsSubmissions(SurveySubmissionDTO surveySubmissionDTO, Survey surveyFromDB) {
        List<QuestionSubmissionDTO> questionSubmissionDTOs = surveySubmissionDTO.getQuestionSubmissions();

        return questionSubmissionDTOs
                .stream()
                .map(questionSubmissionDTO -> createQuestionSubmissionUsingClientInput(questionSubmissionDTO, surveyFromDB))
                .collect(Collectors.toList());
    }

    private QuestionSubmission createQuestionSubmissionUsingClientInput(QuestionSubmissionDTO questionSubmissionDTO, Survey survey){
        Question question = getQuestionFromSurvey(questionSubmissionDTO, survey);
        Choice choice = getChoiceFromSurvey(questionSubmissionDTO, question);

        return new QuestionSubmission(question, choice);
    }

    private Choice getChoiceFromSurvey(QuestionSubmissionDTO questionSubmissionDTO, Question question) {
        return question
                .getChoices()
                .stream()
                .filter(c -> c.getId().equals(questionSubmissionDTO.getChoiceId()))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }

    private Question getQuestionFromSurvey(QuestionSubmissionDTO questionSubmissionDTO, Survey survey) {
        return survey.getQuestions()
                .stream()
                .filter(q -> q.getId().equals(questionSubmissionDTO.getQuestionId()))
                .findAny()
                .orElseThrow(EntityNotFoundException::new);
    }

    private boolean nonMatchingChoicesIdsExists(SurveySubmissionDTO surveySubmissionDTO, List<Integer> choicesIdsFromDB) {
        return surveySubmissionDTO
                .getQuestionSubmissions()
                .stream()
                .map(QuestionSubmissionDTO::getChoiceId)
                .anyMatch(choicesId -> !choicesIdsFromDB.contains(choicesId));
    }

    private List<Integer> getChoicesIds(Survey surveyFromDB) {
        return surveyFromDB
                .getQuestions()
                .stream()
                .flatMap(question -> question.getChoices().stream())
                .map(Choice::getId)
                .collect(Collectors.toList());
    }

    private List<Integer> getSurveyQuestionsIds(Survey surveyFromDB) {
        return surveyFromDB
                .getQuestions()
                .stream()
                .map(Question::getId)
                .collect(Collectors.toList());
    }

}
