package com.codeit.survey.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SurveyStatisticsDTO {
    private Integer surveyId;
    private String surveyName;
    private int numberOfSubmissions;
    private List<QuestionStatisticsDTO> questionStatistics;
}
