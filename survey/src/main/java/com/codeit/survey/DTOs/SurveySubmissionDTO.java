package com.codeit.survey.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SurveySubmissionDTO {
    @NotNull(message = "Survey ID can't be null")
    Integer surveyId;

    @NotEmpty(message = "QuestionSubmissions can't be empty")
    @Valid
    List<QuestionSubmissionDTO> questionSubmissions;
}
