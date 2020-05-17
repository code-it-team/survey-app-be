package com.codeit.survey.serviceTest;

import com.codeit.survey.DTOs.QuestionSubmissionDTO;
import com.codeit.survey.DTOs.SurveySubmissionDTO;
import com.codeit.survey.entities.*;
import com.codeit.survey.repositories.SurveyRepo;
import com.codeit.survey.services.SurveyService;
import com.codeit.survey.services.SurveySubmissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SurveySubmissionServiceTest {

    @InjectMocks
    SurveySubmissionService surveySubmissionService;

    @Mock
    SurveyService surveyServiceMock;

    @Mock
    SurveyRepo surveyRepoMock;

    @Test
    void givenASurveySubmissionDTO_DetectSurveyDoesntExist(){
        SurveySubmissionDTO surveySubmissionDTO = new SurveySubmissionDTO();
        surveySubmissionDTO.setSurveyId(12);

        doReturn(null).when(surveyServiceMock).findById(12);

        ResponseEntity result = surveySubmissionService.checkSurveyAndSubmit(surveySubmissionDTO);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("The Survey 12 doesn't exist", result.getBody());
    }

    @Test
    void givenASurveySubmissionDTO_DetectSurveyIsntPublished(){
        SurveySubmissionDTO surveySubmissionDTO = new SurveySubmissionDTO();
        surveySubmissionDTO.setSurveyId(10);

        Survey survey = new Survey();
        survey.setId(10);
        doReturn(survey).when(surveyServiceMock).findById(10);

        ResponseEntity result = surveySubmissionService.checkSurveyAndSubmit(surveySubmissionDTO);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("The survey 10 isn't published", result.getBody());
    }

    @Test
    void givenASurveySubmissionDTO_DetectQuestionCountIsntCorrect(){
        SurveySubmissionDTO surveySubmissionDTO = new SurveySubmissionDTO();
        surveySubmissionDTO.setSurveyId(22);

        QuestionSubmissionDTO questionSubmissionDTO1 = new QuestionSubmissionDTO(1, 2);

        List<QuestionSubmissionDTO> questionSubmissionDTOs = Collections.singletonList(questionSubmissionDTO1);

        surveySubmissionDTO.setQuestionSubmissions(questionSubmissionDTOs);

        Survey survey = new Survey();
        survey.setSurveyPublication(new SurveyPublication());
        Question question1 = new Question();
        Question question2 = new Question();
        survey.setQuestions(Arrays.asList(question1, question2));

        doReturn(survey).when(surveyServiceMock).findById(22);

        ResponseEntity result = surveySubmissionService.checkSurveyAndSubmit(surveySubmissionDTO);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("The number of questions submitted (1) isn't correct", result.getBody());
    }

    @Test
    void givenASurveySubmissionDTO_DetectNonMatchingQuestion(){
        SurveySubmissionDTO surveySubmissionDTO = new SurveySubmissionDTO();
        surveySubmissionDTO.setSurveyId(100);

        QuestionSubmissionDTO questionSubmissionDTO1 = new QuestionSubmissionDTO(1, 2);
        QuestionSubmissionDTO questionSubmissionDTO2 = new QuestionSubmissionDTO(10, 20);


        List<QuestionSubmissionDTO> questionSubmissionDTOs = Arrays.asList(questionSubmissionDTO1, questionSubmissionDTO2);

        surveySubmissionDTO.setQuestionSubmissions(questionSubmissionDTOs);

        Survey survey = new Survey();
        survey.setSurveyPublication(new SurveyPublication());
        Question question1 = new Question();
        question1.setId(1);
        Question question2 = new Question();
        question2.setId(11);
        survey.setQuestions(Arrays.asList(question1, question2));

        doReturn(survey).when(surveyServiceMock).findById(100);

        ResponseEntity result = surveySubmissionService.checkSurveyAndSubmit(surveySubmissionDTO);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Some Question Ids aren't correct", result.getBody());
    }

    @Test
    void givenASurveySubmissionDTO_DetectNonMatchingChoices(){
        SurveySubmissionDTO surveySubmissionDTO = new SurveySubmissionDTO();
        surveySubmissionDTO.setSurveyId(100);

        QuestionSubmissionDTO questionSubmissionDTO1 = new QuestionSubmissionDTO(1, 2);
        QuestionSubmissionDTO questionSubmissionDTO2 = new QuestionSubmissionDTO(10, 20);


        List<QuestionSubmissionDTO> questionSubmissionDTOs = Arrays.asList(questionSubmissionDTO1, questionSubmissionDTO2);

        surveySubmissionDTO.setQuestionSubmissions(questionSubmissionDTOs);

        Survey survey = new Survey();
        survey.setSurveyPublication(new SurveyPublication());

        Question question1 = new Question();
        question1.setId(1);
        Choice choice11 = new Choice();
        choice11.setId(2);
        Choice choice12 = new Choice();
        choice12.setId(22);
        question1.setChoices(Arrays.asList(choice11, choice12));


        Question question2 = new Question();
        question2.setId(10);
        Choice choice21 = new Choice();
        choice21.setId(23);
        Choice choice22 = new Choice();
        choice22.setId(24);
        question2.setChoices(Arrays.asList(choice21, choice22));

        survey.setQuestions(Arrays.asList(question1, question2));

        doReturn(survey).when(surveyServiceMock).findById(100);

        ResponseEntity result = surveySubmissionService.checkSurveyAndSubmit(surveySubmissionDTO);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Some Choices Ids aren't correct", result.getBody());
    }

    @Test
    void givenASurveySubmissionDTO_CreateANewSurveySubmission(){
        SurveySubmissionDTO surveySubmissionDTO = new SurveySubmissionDTO();
        surveySubmissionDTO.setSurveyId(123);

        QuestionSubmissionDTO questionSubmissionDTO1 = new QuestionSubmissionDTO(9, 8);
        QuestionSubmissionDTO questionSubmissionDTO2 = new QuestionSubmissionDTO(90, 80);


        List<QuestionSubmissionDTO> questionSubmissionDTOs = Arrays.asList(questionSubmissionDTO1, questionSubmissionDTO2);

        surveySubmissionDTO.setQuestionSubmissions(questionSubmissionDTOs);

        Survey survey = new Survey();
        survey.setSurveyPublication(new SurveyPublication());

        Question question1 = new Question();
        question1.setId(9);
        Choice choice11 = new Choice();
        choice11.setId(8);
        Choice choice12 = new Choice();
        choice12.setId(22);
        question1.setChoices(Arrays.asList(choice11, choice12));


        Question question2 = new Question();
        question2.setId(90);
        Choice choice21 = new Choice();
        choice21.setId(80);
        Choice choice22 = new Choice();
        choice22.setId(24);
        question2.setChoices(Arrays.asList(choice21, choice22));

        survey.setQuestions(Arrays.asList(question1, question2));

        doReturn(survey).when(surveyServiceMock).findById(123);

        doReturn(survey).when(surveyRepoMock).save(survey);

        ResponseEntity result = surveySubmissionService.checkSurveyAndSubmit(surveySubmissionDTO);

        assertEquals(HttpStatus.OK, result.getStatusCode());

        SurveySubmission correspondingSurveySubmission = survey.getSurveyPublication().getSurveySubmissionList().get(0);

        assertEquals(9, correspondingSurveySubmission.getQuestionSubmissions().get(0).getQuestion().getId());
        assertEquals(90, correspondingSurveySubmission.getQuestionSubmissions().get(1).getQuestion().getId());

        assertEquals(8, correspondingSurveySubmission.getQuestionSubmissions().get(0).getSelectedChoice().getId());
        assertEquals(80, correspondingSurveySubmission.getQuestionSubmissions().get(1).getSelectedChoice().getId());

    }

}
