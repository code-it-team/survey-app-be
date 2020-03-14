package com.codeit.survey.DTOs.EntityDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SurveysDTO {
    List<SurveyDTO> surveyDTOS;
}
