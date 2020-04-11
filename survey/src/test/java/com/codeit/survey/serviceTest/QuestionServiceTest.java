package com.codeit.survey.serviceTest;

import com.codeit.survey.DTOs.EntityDTOs.QuestionDTO;
import com.codeit.survey.entities.Choice;
import com.codeit.survey.entities.Question;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.repositories.QuestionRepo;
import com.codeit.survey.services.ChoiceService;
import com.codeit.survey.services.QuestionService;
import com.codeit.survey.services.SurveyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {
    @InjectMocks
    QuestionService questionService;

    @Mock
    ChoiceService choiceServiceMock;

    @Mock
    SurveyService surveyServiceMock;

    @Mock
    QuestionRepo questionRepoMock;

    @Test
    void givenTwoQuestions_TwoQuestionDTOsAreCreatedSuccessfully(){
        Question question1 = new Question(1, "TEST BODY1", null, null);
        Question question2 = new Question(2, "TEST BODY2", null, null);

        doReturn(null).when(choiceServiceMock).createDTOsFromChoices(any());

        List<QuestionDTO> questionDTOS = questionService.createDTOsFromQuestions(Arrays.asList(question1, question2));

        assertEquals(1 ,questionDTOS.get(0).getId());
        assertEquals( "TEST BODY1", questionDTOS.get(0).getBody());

        assertEquals(2, questionDTOS.get(1).getId());
        assertEquals("TEST BODY2", questionDTOS.get(1).getBody());
    }

    @Test
    void givenAQuestion_ItsSuccessfullyAddedToItsSurvey(){
        Survey DTOSurvey = new Survey();
        DTOSurvey.setId(1);

        Survey DBSurvey = new Survey();
        DBSurvey.setId(1);
        DBSurvey.setName("DB SURVEY NAME");

        doReturn(DBSurvey).when(surveyServiceMock).findById(1);

        Choice choice = new Choice();

        Question question = new Question(1, "TEST BODY", DTOSurvey, Collections.singletonList(choice));

        doReturn(question).when(questionRepoMock).save(question);

        ResponseEntity responseEntity =  questionService.addQuestionToSurvey_admin(question);

        assertEquals("DB SURVEY NAME", question.getSurvey().getName());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void givenAQuestion_ItsBodyIsSuccessfullyUpdated(){
        Question DTOQuestion = new Question(1, "TEST BODY UPDATED", null, null);
        Question DBQuestion = new Question(1, "TEST BODY", null, null);

        doReturn(Optional.of(DBQuestion)).when(questionRepoMock).findById(1);
        doReturn(DBQuestion).when(questionRepoMock).save(DBQuestion);

        ResponseEntity responseEntity = questionService.updateSurveyQuestion_admin(DTOQuestion);
        assertEquals("TEST BODY UPDATED", DBQuestion.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }



}
