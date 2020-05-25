package com.codeit.survey.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class QuestionSubmissionDTO {
    @NotNull(message = "Question ID can't be null")
    Integer questionId;
    @NotNull(message = "Choice ID can't be null")
    Integer choiceId;
}
