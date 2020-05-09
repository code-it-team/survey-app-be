package com.codeit.survey.aspects;

import com.codeit.survey.entities.Choice;
import com.codeit.survey.entities.Question;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.services.VerificationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class VerificationAspect {

    private VerificationService verificationService;

    @Autowired
    public VerificationAspect(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @Around("@annotation(com.codeit.survey.aspects.annotations.VerifySurveyBelongToUser)")
    public Object verifyUser(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Integer surveyId = getSurveyIdFromObject(proceedingJoinPoint);

        if(verificationService.notUserSurvey(surveyId)){
            String message = String.format("The Survey %s doesn't belong to the user", surveyId);
            return ResponseEntity.badRequest().body(message);
        }
        else {
            return proceedingJoinPoint.proceed();
        }
    }

    private Integer getSurveyIdFromObject(ProceedingJoinPoint proceedingJoinPoint) {
        Object objectToBeVerified = proceedingJoinPoint.getArgs()[0];

        switch (objectToBeVerified.getClass().getSimpleName()){
            case ("Choice"):
                return getSurveyIdFromChoice((Choice) objectToBeVerified);
            case ("Question"):
                return getSurveyIdFromQuestion((Question) objectToBeVerified);
            case("Survey"):
                return ((Survey) objectToBeVerified).getId();
            case("Integer"):
                return ((Integer) objectToBeVerified);
            default:
                throw new IllegalArgumentException();
        }
    }

    private Integer getSurveyIdFromQuestion(Question objectToBeVerified) {
        return objectToBeVerified.getSurvey().getId();
    }

    private Integer getSurveyIdFromChoice(Choice objectToBeVerified) {
        return objectToBeVerified.getQuestion().getSurvey().getId();
    }
}
