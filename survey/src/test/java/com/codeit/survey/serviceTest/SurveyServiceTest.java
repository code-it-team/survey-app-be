package com.codeit.survey.serviceTest;

import com.codeit.survey.DTOs.DTOService.SurveyDTOService;
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
import com.codeit.survey.services.adminServices.SurveyServiceAdmin;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {

    @InjectMocks
    SurveyServiceAdmin surveyServiceAdmin;

    @InjectMocks
    SurveyDTOService surveyDTOService;

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

        Survey survey = new Survey(1,false ,null, null, "TEST NAME", Collections.singletonList(question));

        doReturn(survey).when(surveyRepoMock).save(survey);

        ResponseEntity responseEntity = surveyServiceAdmin.checkAndAddSurvey(survey);

        assertEquals("TEST QUESTION 1", choice1.getQuestion().getBody());
        assertEquals("TEST QUESTION 1", choice2.getQuestion().getBody());

        assertEquals("TEST NAME", question.getSurvey().getName());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void givenASurveyComingWithRequestFromClient_DetectTheConflictingSurveyName(){
        Survey survey = new Survey(1, false, null, null, "TEST NAME", null);
        doReturn(true).when(surveyRepoMock).existsByName("TEST NAME");
        ResponseEntity responseEntity = surveyServiceAdmin.checkAndAddSurvey(survey);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void givenASurveyComingWithRequestFromClient_ShouldReturnBadRequest_WhenSurveyIsPublished(){
        Survey clientSurvey = new Survey();
        clientSurvey.setId(1);

        Survey dbSurvey = new Survey();
        dbSurvey.setPublished(true);

        doReturn(Optional.of(dbSurvey)).when(surveyRepoMock).findById(1);

        ResponseEntity responseEntity = surveyServiceAdmin.checkAndUpdateSurvey(clientSurvey);
        assertEquals(HttpStatus.BAD_REQUEST ,responseEntity.getStatusCode());
        assertEquals("Survey can't be updated because it's published", responseEntity.getBody());
    }
    @Test
    void givenASurvey_CreateItsSurveyDTOSuccessfully(){
        LocalDateTime localDateTime = LocalDateTime.now();

        Survey survey =new Survey(1, false, null, localDateTime, "TEST NAME", null);

        doReturn(null).when(userServiceMock).createDTOFromSurveyUser(null);
        doReturn(null).when(questionServiceMock).createDTOsFromQuestions(null);

        SurveyDTO surveyDTO = surveyDTOService.createDTOFromSurvey(survey);

        assertEquals("TEST NAME", surveyDTO.getName());
        assertEquals(localDateTime, surveyDTO.getCreationDate());
        assertEquals(1, surveyDTO.getId());
    }

}
