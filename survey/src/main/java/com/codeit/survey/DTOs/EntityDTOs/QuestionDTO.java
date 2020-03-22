package com.codeit.survey.DTOs.EntityDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class QuestionDTO {
    private Integer id;
    private String body;
    private List<ChoiceDTO> choices;
}
