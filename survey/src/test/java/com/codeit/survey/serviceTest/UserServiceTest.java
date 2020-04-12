package com.codeit.survey.serviceTest;

import com.codeit.survey.DTOs.EntityDTOs.SurveyUserDTO;
import com.codeit.survey.entities.Authority;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.UserRepo;
import com.codeit.survey.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
   @InjectMocks
    UserService userService;

   @Mock
    UserRepo userRepoMock;

   @Test
    void givenAUser_ADTOIsCreatedSuccessfully(){
        Authority authority = new Authority("USER", null);
        SurveyUser surveyUser = new SurveyUser("USERNAME TEST", "PASSWORD TEST", true, authority, null);
        surveyUser.setId(1);

        SurveyUserDTO surveyUserDTO = userService.createDTOFromSurveyUser(surveyUser);

        assertEquals(1, surveyUserDTO.getId());
        assertEquals("USERNAME TEST", surveyUserDTO.getUsername());
        assertEquals("USER", surveyUserDTO.getAuthorities().get(0).getRole());
    }

    @Test
    void givenAnExistingUserName_WhenComingFromClient_ReturnAConflictStatus(){
       SurveyUser surveyUser = new SurveyUser();
       surveyUser.setUsername("TEST USERNAME");
       doReturn(Optional.of(surveyUser)).when(userRepoMock).findByUsername("TEST USERNAME");
        ResponseEntity responseEntity = userService.addUser(surveyUser);
       assertEquals(HttpStatus.CONFLICT,responseEntity.getStatusCode());
    }

}
