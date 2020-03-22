package com.codeit.survey.DTOs.EntityDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ChoiceDTO {
    private Integer id;
    private String body;
}
