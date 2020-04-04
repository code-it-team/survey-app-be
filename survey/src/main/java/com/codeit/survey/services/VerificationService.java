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
     * @return true if the survey belong to surveys of the user
     */
    public boolean notUserSurvey(Integer surveyId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SurveyUser surveyUser = userService.getSurveyUserByUserName
                (
                        ((CustomUserDetails)auth.getPrincipal()).
                                getUsername()
                );
        if (surveyUser == null) return false;

        List<Integer> userSurveysIds =
                surveyService.
                        getSurveysByUserId(surveyUser.getId()).
                        stream().
                        map(Survey::getId).
                        collect(Collectors.toList());

        Survey survey = surveyService.findById(surveyId);

        return survey == null || !userSurveysIds.contains(survey.getId());
    }
}
