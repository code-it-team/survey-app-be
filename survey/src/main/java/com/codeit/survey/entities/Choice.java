package com.codeit.survey.entities;

import com.codeit.survey.controllers.validationInterface.ChoiceCreation;
import com.codeit.survey.controllers.validationInterface.QuestionCreation;
import com.codeit.survey.controllers.validationInterface.SurveyCreation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(max = 200, message = "Choice content length can't exceed 200 characters", groups = {SurveyCreation.class, QuestionCreation.class, ChoiceCreation.class})
    @NotBlank(message = "The Choice can't have an empty body", groups = {SurveyCreation.class, QuestionCreation.class, ChoiceCreation.class})
    private String body;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    @NotNull(message = "A question must be specified", groups = {ChoiceCreation.class})
    @Valid
    private Question question;

}
