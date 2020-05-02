package com.codeit.survey.DTOs.EntitiyListDTOs;

import com.codeit.survey.DTOs.EntityDTOs.SurveyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SurveysDTO {
    List<SurveyDTO> surveys;
}
