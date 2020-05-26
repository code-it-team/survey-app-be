package com.codeit.survey.services;

import com.codeit.survey.DTOs.ChoiceStatisticsDTO;
import com.codeit.survey.DTOs.QuestionStatisticsDTO;
import com.codeit.survey.DTOs.SurveyStatisticsDTO;
import com.codeit.survey.aspects.annotations.VerifySurveyBelongToUser;
import com.codeit.survey.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyStatisticsService {
    private SurveyService surveyService;

    @Autowired
    public SurveyStatisticsService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @VerifySurveyBelongToUser
    public ResponseEntity<?> getSurveyStatistics(Integer surveyId) {
        return getSurveyStatistics_admin(surveyId);
    }

    private ResponseEntity<?> getSurveyStatistics_admin(Integer surveyId){
        Survey survey = surveyService.findById(surveyId);

        if (survey == null) return ResponseEntity.badRequest().body(String.format("The Survey %s doesn't exist", surveyId));
        if (!survey.isPublished()) return ResponseEntity.badRequest().body(String.format("The Survey %s isn't published yet", surveyId));

        SurveyStatisticsDTO surveyStatisticsDTO = createSurveyStatisticsDTOFromSurvey(survey);
        return ResponseEntity.ok(surveyStatisticsDTO);
    }

    private SurveyStatisticsDTO createSurveyStatisticsDTOFromSurvey(Survey survey) {
        List<QuestionStatisticsDTO> questionStatisticsDTOS = createQuestionStatisticsDTOs(survey);

        int numberOfSubmissions = survey.getSurveyPublication().getSurveySubmissionList().size();

        return new SurveyStatisticsDTO(survey.getId(), survey.getName(), numberOfSubmissions, questionStatisticsDTOS);
    }

    private List<QuestionStatisticsDTO> createQuestionStatisticsDTOs(Survey survey) {
        return survey
                .getQuestions()
                .stream()
                .map(question -> createQuestionStatisticsDTO(question, survey))
                .collect(Collectors.toList());
    }

    private QuestionStatisticsDTO createQuestionStatisticsDTO(Question question, Survey survey) {
        List<QuestionSubmission> questionSubmissions = getQuestionSubmissionsOfAQuestion(question, survey);

        List<ChoiceStatisticsDTO> choiceStatisticsDTOS = question
                .getChoices()
                .stream()
                .map(choice -> createChoiceStatisticsDTO(choice, questionSubmissions))
                .collect(Collectors.toList());

        return new QuestionStatisticsDTO(question.getId(), question.getBody(), choiceStatisticsDTOS);
    }

    private ChoiceStatisticsDTO createChoiceStatisticsDTO(Choice choice, List<QuestionSubmission> questionSubmissions) {
        List<QuestionSubmission> questionSubmissionsWithTheGivenChoice = questionSubmissions
                .stream()
                .filter(questionSubmission -> questionSubmission.getSelectedChoice().getId().equals(choice.getId()))
                .collect(Collectors.toList());

        float percentageOfSubmissionsForTheGivenQuestion = questionSubmissionsWithTheGivenChoice.size() / (float) questionSubmissions.size();

        return new ChoiceStatisticsDTO(choice.getId(), choice.getBody(), percentageOfSubmissionsForTheGivenQuestion);
    }

    private List<QuestionSubmission> getQuestionSubmissionsOfAQuestion(Question question, Survey survey) {
        return survey.getSurveyPublication().getSurveySubmissionList()
                .stream()
                .flatMap(surveySubmission -> surveySubmission.getQuestionSubmissions().stream())
                .filter(questionSubmission -> questionSubmission.getQuestion().getId().equals(question.getId()))
                .collect(Collectors.toList());
    }


}
