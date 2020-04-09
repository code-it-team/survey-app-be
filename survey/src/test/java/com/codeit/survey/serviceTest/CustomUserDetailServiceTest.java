package com.codeit.survey.serviceTest;

import com.codeit.survey.entities.Authority;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.UserRepo;
import com.codeit.survey.security.CustomUserDetails;
import com.codeit.survey.services.CustomUserDetailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {
    @InjectMocks
    private CustomUserDetailService customUserDetailService;
    @Mock
    private UserRepo userRepoMock;

    @Test
    void loadUserBuUsernameTest_userExists(){
        Authority authority = new Authority("USER", null);
        SurveyUser surveyUser = new SurveyUser("testUsername", "testPassword", true, authority, null);

        when(userRepoMock.findByUsername("testUsername")).thenReturn(Optional.of(surveyUser));
        UserDetails userDetails = customUserDetailService.loadUserByUsername("testUsername");

        assertEquals(userDetails.getPassword(), "testPassword");
        assertEquals(userDetails.getUsername(), "testUsername");
        assertEquals(userDetails.getAuthorities().size(), 1);
    }

    @Test
    void loadUserBuUsernameTest_userNotExists(){
        when(userRepoMock.findByUsername("testUsername")).thenReturn(Optional.empty());
        assertThrows(
                BadCredentialsException.class,
                () -> customUserDetailService.loadUserByUsername("testUsername"));
    }

}
