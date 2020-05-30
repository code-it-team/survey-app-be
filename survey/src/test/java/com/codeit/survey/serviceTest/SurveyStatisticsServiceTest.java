package com.codeit.survey.serviceTest;

import com.codeit.survey.DTOs.QuestionStatisticsDTO;
import com.codeit.survey.DTOs.SurveyStatisticsDTO;
import com.codeit.survey.entities.*;
import com.codeit.survey.services.SurveyService;
import com.codeit.survey.services.SurveyStatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class SurveyStatisticsServiceTest {
    @InjectMocks
    SurveyStatisticsService surveyStatisticsService;

    @Mock
    SurveyService surveyServiceMock;

    @Test
    void givenASurveyIdToGetStatistics_DetectItDoesntExist(){
        doReturn(null).when(surveyServiceMock).findById(1);

        ResponseEntity result = surveyStatisticsService.getSurveyStatistics_admin(1);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("The Survey 1 doesn't exist", result.getBody());
    }

    @Test
    void givenASurveyIdToGetStatistics_DetectItsNotPublished(){
        Survey survey = new Survey();
        doReturn(survey).when(surveyServiceMock).findById(2);

        ResponseEntity result = surveyStatisticsService.getSurveyStatistics_admin(2);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("The Survey 2 isn't published yet", result.getBody());
    }

    @Test
    void givenASurveyIdToGetStatistics_GetItsSurveyStatisticsDTO(){
        Survey survey = createASurveyWithOneQuestionAndTwoSubmissions();

        doReturn(survey).when(surveyServiceMock).findById(10);

        ResponseEntity result = surveyStatisticsService.getSurveyStatistics_admin(10);
        SurveyStatisticsDTO surveyStatisticsDTO = (SurveyStatisticsDTO) result.getBody();


        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, surveyStatisticsDTO.getNumberOfSubmissions());
        assertEquals("SURVEY", surveyStatisticsDTO.getSurveyName());
        assertEquals(10, surveyStatisticsDTO.getSurveyId());

        assertEquals(1, surveyStatisticsDTO.getQuestionStatistics().size());

        QuestionStatisticsDTO questionStatisticsDTO = surveyStatisticsDTO.getQuestionStatistics().get(0);
        assertEquals("QUESTION", questionStatisticsDTO.getQuestionBody());
        assertEquals(20, questionStatisticsDTO.getQuestionId());

        assertEquals(2, questionStatisticsDTO.getChoiceStatistics().size());
        assertEquals(50.0f, questionStatisticsDTO.getChoiceStatistics().get(0).getPercentageOfSubmissions());
        assertEquals(50.0f, questionStatisticsDTO.getChoiceStatistics().get(1).getPercentageOfSubmissions());
    }

    private Survey createASurveyWithOneQuestionAndTwoSubmissions() {
        Survey survey = new Survey(10, null, null, null, "SURVEY",  null);

        Question question = new Question(20, "QUESTION", survey, null);


        Choice choice1 = new Choice(30, "CHOICE 1", question);
        Choice choice2 = new Choice(40, "CHOICE 2", question);

        question.setChoices(Arrays.asList(choice1, choice2));

        survey.setQuestions(Collections.singletonList(question));

        SurveyPublication surveyPublication = new SurveyPublication("LINK", null, survey);

        SurveySubmission surveySubmission1 = new SurveySubmission();
        QuestionSubmission questionSubmission1 = new QuestionSubmission(question, choice1);

        surveySubmission1.setSurveyPublication(surveyPublication);
        surveySubmission1.setQuestionSubmissions(Arrays.asList(questionSubmission1));


        SurveySubmission surveySubmission2 = new SurveySubmission();
        QuestionSubmission questionSubmission2 = new QuestionSubmission(question, choice2);

        surveySubmission2.setSurveyPublication(surveyPublication);
        surveySubmission2.setQuestionSubmissions(Arrays.asList(questionSubmission2));


        surveyPublication.setSurveySubmissionList(Arrays.asList(surveySubmission1, surveySubmission2));
        survey.setSurveyPublication(surveyPublication);
        return survey;
    }


}
