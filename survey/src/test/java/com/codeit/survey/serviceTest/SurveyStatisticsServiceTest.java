package com.codeit.survey.serviceTest;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    void givenASurveyIdToGetStatistics_QuestionListIsEmpty(){
        List<Question> questionList = new ArrayList<>();
        Survey survey = new Survey(3, null, null, null, "TEST NAME", questionList);

        SurveyPublication surveyPublication = new SurveyPublication();
        surveyPublication.setSurvey(survey);
        surveyPublication.setSurveySubmissionList(Arrays.asList(new SurveySubmission(), new SurveySubmission()));

        survey.setSurveyPublication(surveyPublication);

        doReturn(survey).when(surveyServiceMock).findById(3);

        ResponseEntity result = surveyStatisticsService.getSurveyStatistics_admin(3);
        SurveyStatisticsDTO surveyStatisticsDTO = (SurveyStatisticsDTO) result.getBody();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("TEST NAME", surveyStatisticsDTO.getSurveyName());
        assertEquals(3, surveyStatisticsDTO.getSurveyId());
        assertEquals(2, surveyStatisticsDTO.getNumberOfSubmissions());
        assertEquals(0, surveyStatisticsDTO.getQuestionStatistics().size());
    }





}
