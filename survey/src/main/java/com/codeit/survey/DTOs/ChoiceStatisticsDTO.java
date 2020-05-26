package com.codeit.survey.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ChoiceStatisticsDTO {
    private Integer choiceId;
    private String choiceBody;
    private float percentageOfSubmissions;
}
