package com.codeit.survey.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SurveySubmissionDTO {
    Integer surveyId;
    List<QuestionSubmissionDTO> questionSubmissions;
}
