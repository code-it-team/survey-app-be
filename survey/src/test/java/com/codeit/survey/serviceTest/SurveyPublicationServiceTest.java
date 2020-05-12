package com.codeit.survey.serviceTest;

import com.codeit.survey.entities.Survey;
import com.codeit.survey.repositories.SurveyRepo;
import com.codeit.survey.services.SurveyPublicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SurveyPublicationServiceTest {
    @InjectMocks
    SurveyPublicationService surveyPublicationService;

    @Mock
    SurveyRepo surveyRepoMock;

    @Test
    void givenASurvey_GenerateAPublicationLink(){
        String clientURL = "https/test/publishSurvey";

        Survey survey = new Survey();
        survey.setId(123);

        doReturn(survey).when(surveyRepoMock).save(survey);

        surveyPublicationService.publishSurvey(survey, clientURL);

        assertEquals("https/test/submitSurvey/123", survey.getSurveyPublication().getLink());
    }



}
