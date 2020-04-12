package com.codeit.survey.services;

import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VerificationService {
    private UserService userService;
    private SurveyService surveyService;

    @Autowired
    @Lazy
    public VerificationService(UserService userService, SurveyService surveyService){
        this.userService = userService;
        this.surveyService = surveyService;
    }

    /**
     * @return true if the survey doesn't exist or doesn't belong to surveys of the user
     */
    public boolean notUserSurvey(Integer surveyId){
        SurveyUser surveyUser = getAuthenticatedSurveyUser();
        if (surveyUser == null) return false;

        List<Integer> userSurveysIds = getUsersSurveysIds(surveyUser);

        Survey survey = surveyService.findById(surveyId);

        return survey == null || !userSurveysIds.contains(survey.getId());
    }

    public List<Integer> getUsersSurveysIds(SurveyUser surveyUser) {
        List<Survey> UsersSurveys = surveyService.getSurveysByUserId(surveyUser.getId());
        return UsersSurveys.
                stream().
                map(Survey::getId).
                collect(Collectors.toList());
    }

    public SurveyUser getAuthenticatedSurveyUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((CustomUserDetails) auth.getPrincipal()).getUsername();
        return userService.getSurveyUserByUserName(username);
    }
}
