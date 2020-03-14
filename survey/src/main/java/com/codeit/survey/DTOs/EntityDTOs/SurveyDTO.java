package com.codeit.survey.DTOs.EntityDTOs;


import com.codeit.survey.entities.Survey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SurveyDTO {
    private Integer id;
    private LocalDateTime creationDate;
    private String name;
    SurveyUserDTO surveyUserDTO;
}
