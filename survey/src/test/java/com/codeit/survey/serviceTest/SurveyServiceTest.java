package com.codeit.survey.serviceTest;

import com.codeit.survey.DTOs.EntitiyListDTOs.SurveysDTO;
import com.codeit.survey.DTOs.EntityDTOs.SurveyDTO;
import com.codeit.survey.entities.Choice;
import com.codeit.survey.entities.Question;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.SurveyRepo;
import com.codeit.survey.services.QuestionService;
import com.codeit.survey.services.SurveyService;
import com.codeit.survey.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {

    @InjectMocks
    SurveyService surveyService;

    @Mock
    SurveyRepo surveyRepoMock;

    @Mock
    UserService userServiceMock;

    @Mock
    QuestionService questionServiceMock;


    @Test
    void givenASurveyComingWithRequestFromClient_SuccessfullyAddItToDb(){
        Choice choice1 = new Choice(1, "TEST CHOICE 1", null);
        Choice choice2 = new Choice(2, "TEST CHOICE 2", null);

        Question question = new Question(1, "TEST QUESTION 1", null ,Arrays.asList(choice1, choice2));

        Survey survey = new Survey(1, null, null, "TEST NAME", Collections.singletonList(question));

        doReturn(survey).when(surveyRepoMock).save(survey);

        ResponseEntity responseEntity = surveyService.checkAndAddSurvey(survey);

        assertEquals("TEST QUESTION 1", choice1.getQuestion().getBody());
        assertEquals("TEST QUESTION 1", choice2.getQuestion().getBody());

        assertEquals("TEST NAME", question.getSurvey().getName());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void givenASurveyComingWithRequestFromClient_DetectTheConflictingSurveyName(){
        Survey survey = new Survey(1, null, null, "TEST NAME", null);
        doReturn(true).when(surveyRepoMock).existsByName("TEST NAME");
        ResponseEntity responseEntity = surveyService.checkAndAddSurvey(survey);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void givenAUserId_ReturnItsSurveyDTOs(){
        SurveyUser surveyUser = new SurveyUser();
        LocalDateTime localDateTime = LocalDateTime.now();

        List<Survey> surveys = Collections.singletonList(new Survey(1, surveyUser, localDateTime, "TEST NAME", null));

        doReturn(surveyUser).when(userServiceMock).getUserById(10);
        doReturn(surveys).when(surveyRepoMock).findSurveysBySurveyUser(surveyUser);

        doReturn(null).when(userServiceMock).createDTOFromSurveyUser(surveyUser);
        doReturn(null).when(questionServiceMock).createDTOsFromQuestions(null);

        ResponseEntity responseEntity = surveyService.getSurveysByUserId_response(10);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        SurveysDTO surveysDTO = (SurveysDTO) responseEntity.getBody();
        assertEquals(1, surveysDTO.getSurveyDTOS().size());

        SurveyDTO surveyDTO = surveysDTO.getSurveyDTOS().get(0);

        assertEquals("TEST NAME", surveyDTO.getName());
        assertEquals(localDateTime, surveyDTO.getCreationDate());
        assertEquals(1, surveyDTO.getId());
    }


}
