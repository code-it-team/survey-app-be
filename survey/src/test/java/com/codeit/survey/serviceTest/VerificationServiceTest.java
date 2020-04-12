package com.codeit.survey.serviceTest;

import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.services.SurveyService;
import com.codeit.survey.services.UserService;
import com.codeit.survey.services.VerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class VerificationServiceTest {

    @Mock
    SurveyService surveyServiceMock;
    @Mock
    UserService userServiceMock;
    @InjectMocks
    VerificationService verificationService = spy(new VerificationService(userServiceMock, surveyServiceMock));


    @Test
    void givenASurveyIdOf2_WhenSurveyBelongsToAuthenticatedUser_ReturnFalse(){
        SurveyUser surveyUser = new SurveyUser();
        surveyUser.setId(1);
        doReturn(surveyUser).when(verificationService).getAuthenticatedSurveyUser();
        doReturn(Arrays.asList(1, 2, 3)).when(verificationService).getUsersSurveysIds(surveyUser);

        Survey survey = new Survey();
        survey.setId(2);
        doReturn(survey).when(surveyServiceMock).findById(2);

        boolean result = verificationService.notUserSurvey(2);
        assertFalse(result);
    }


    @Test
    void givenASurveyIdOf3_WhenSurveyDoesntBelongToAuthenticatedUser_ReturnTrue(){
        SurveyUser surveyUser = new SurveyUser();
        surveyUser.setId(1);
        doReturn(surveyUser).when(verificationService).getAuthenticatedSurveyUser();
        doReturn(Arrays.asList(5, 6, 7)).when(verificationService).getUsersSurveysIds(surveyUser);

        Survey survey = new Survey();
        survey.setId(2);
        doReturn(survey).when(surveyServiceMock).findById(2);

        boolean result = verificationService.notUserSurvey(2);
        assertTrue(result);
    }



}
