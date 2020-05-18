package com.codeit.survey.serviceTest;

import com.codeit.survey.DTOs.DTOService.SurveyDTOService;
import com.codeit.survey.DTOs.EntityDTOs.SurveyDTO;
import com.codeit.survey.DTOs.PublicationLinkDTO;
import com.codeit.survey.entities.*;
import com.codeit.survey.repositories.SurveyRepo;
import com.codeit.survey.services.QuestionService;
import com.codeit.survey.services.UserService;
import com.codeit.survey.services.VerificationService;
import com.codeit.survey.services.adminServices.SurveyServiceAdmin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

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

    @Mock
    VerificationService verificationServiceMock;

    @Test
    void givenASurveyComingWithRequestFromClient_SuccessfullyAddItToDb(){
        Choice choice1 = new Choice(1, "TEST CHOICE 1", null);
        Choice choice2 = new Choice(2, "TEST CHOICE 2", null);

        Question question = new Question(1, "TEST QUESTION 1", null ,Arrays.asList(choice1, choice2));
        Survey survey = new Survey(1,null ,null, null, "TEST NAME", Collections.singletonList(question));

        doReturn(survey).when(surveyRepoMock).save(survey);

        ResponseEntity responseEntity = surveyServiceAdmin.checkAndAddSurvey(survey);

        assertEquals("TEST QUESTION 1", choice1.getQuestion().getBody());
        assertEquals("TEST QUESTION 1", choice2.getQuestion().getBody());

        assertEquals("TEST NAME", question.getSurvey().getName());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void givenASurveyComingWithRequestFromClient_DetectTheConflictingSurveyName(){
        SurveyUser surveyUser = new SurveyUser();
        surveyUser.setId(10);
        Survey survey = new Survey(1, null, null, null, "TEST NAME", null);
        doReturn(surveyUser).when(verificationServiceMock).getAuthenticatedSurveyUser();
        doReturn(true).when(surveyRepoMock).existsByNameAndSurveyUser("TEST NAME", surveyUser);
        ResponseEntity responseEntity = surveyServiceAdmin.checkAndAddSurvey(survey);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void givenASurveyComingWithRequestFromClient_ShouldReturnBadRequest_WhenSurveyIsPublished(){
        Survey clientSurvey = new Survey();
        clientSurvey.setId(1);

        Survey dbSurvey = new Survey();
        SurveyPublication surveyPublication = new SurveyPublication();
        dbSurvey.setSurveyPublication(surveyPublication);

        doReturn(Optional.of(dbSurvey)).when(surveyRepoMock).findById(1);

        ResponseEntity responseEntity = surveyServiceAdmin.checkAndUpdateSurvey(clientSurvey);
        assertEquals(HttpStatus.BAD_REQUEST ,responseEntity.getStatusCode());
        assertEquals("Survey can't be updated because it's published", responseEntity.getBody());
    }
    @Test
    void givenASurvey_CreateItsSurveyDTOSuccessfully(){
        LocalDateTime localDateTime = LocalDateTime.now();
        Survey survey =new Survey(1, null, null, localDateTime, "TEST NAME", null);

        doReturn(null).when(userServiceMock).createDTOFromSurveyUser(null);
        doReturn(null).when(questionServiceMock).createDTOsFromQuestions(null);

        SurveyDTO surveyDTO = surveyDTOService.createDTOFromSurvey(survey);

        assertEquals("TEST NAME", surveyDTO.getName());
        assertEquals(localDateTime, surveyDTO.getCreationDate());
        assertEquals(1, surveyDTO.getId());
    }

    @Test
    void givenAnUpdateForASurvey_SuccessfullyUpdate(){
        SurveyUser surveyUser = new SurveyUser();

        Question newQuestion1 = new Question();
        newQuestion1.setBody("NEW Q 1");
        Question newQuestion2 = new Question();
        newQuestion2.setBody("NEW Q 2");
        List<Question> newQuestions = new ArrayList<>(Arrays.asList(newQuestion1, newQuestion2));
        Survey newSurvey = new Survey(1, null, surveyUser, null, "NEW SURVEY NAME", newQuestions);

        Question oldQuestion1 = new Question();
        Question oldQuestion2 = new Question();
        List<Question> oldQuestions = new ArrayList<>(Arrays.asList(oldQuestion1, oldQuestion2));
        Survey DBSurvey = new Survey(1, null, surveyUser, null, "OLD SURVEY NAME", oldQuestions);

        doReturn(Optional.of(DBSurvey)).when(surveyRepoMock).findById(1);

        doReturn(surveyUser).when(verificationServiceMock).getAuthenticatedSurveyUser();
        doReturn(false).when(surveyRepoMock).existsByNameAndSurveyUser(newSurvey.getName(), surveyUser);
        doReturn(DBSurvey).when(surveyRepoMock).save(DBSurvey);

        ResponseEntity responseEntity = surveyServiceAdmin.checkAndUpdateSurvey(newSurvey);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(DBSurvey.getName(), "NEW SURVEY NAME");
        assertEquals(DBSurvey.getQuestions().get(0).getBody(), "NEW Q 1");
        assertEquals(DBSurvey.getQuestions().get(1).getBody(), "NEW Q 2");
    }

    @Test
    void givenASurveyId_DetectItDoesntExist_WhenGettingPublicationLink(){
        doReturn(Optional.empty()).when(surveyRepoMock).findById(22);

        ResponseEntity result = surveyServiceAdmin.getSurveyPublicationLink(22);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("No such Survey exists", result.getBody());
    }

    @Test
    void givenASurveyId_DetectThatItsNotPublished_WhenGettingPublicationLink(){
        Survey survey = new Survey();
        doReturn(Optional.of(survey)).when(surveyRepoMock).findById(11);

        ResponseEntity result = surveyServiceAdmin.getSurveyPublicationLink(11);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("The Survey hasn't been published yet", result.getBody());
    }

    @Test
    void givenASurveyId_ReturnItsPublicationLinkDTO(){
        Survey survey = new Survey();
        SurveyPublication surveyPublication = new SurveyPublication();
        surveyPublication.setLink("TEST LINK");
        survey.setSurveyPublication(surveyPublication);

        doReturn(Optional.of(survey)).when(surveyRepoMock).findById(22);

        ResponseEntity result = surveyServiceAdmin.getSurveyPublicationLink(22);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        String publicationLink = ((PublicationLinkDTO) result.getBody()).getPublicationLink();
        assertEquals("TEST LINK", publicationLink);
    }

}
