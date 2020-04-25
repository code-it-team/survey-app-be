package com.codeit.survey.entities;

import com.codeit.survey.controllers.validationInterface.ChoiceCreation;
import com.codeit.survey.controllers.validationInterface.QuestionCreation;
import com.codeit.survey.controllers.validationInterface.SurveyCreation;
import com.codeit.survey.controllers.validationInterface.SurveyUpdate;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull(message = "Question ID can't be null", groups = {ChoiceCreation.class})
    private Integer id;

    @Size(max = 1000, message = "Question content length can't exceed 1000 characters", groups = {QuestionCreation.class, SurveyUpdate.class})
    @NotBlank(message = "The Question can't have an empty body", groups = {QuestionCreation.class, SurveyCreation.class, SurveyUpdate.class})
    private String body;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "A survey must be specified", groups = {QuestionCreation.class, ChoiceCreation.class})
    @Valid
    private Survey survey;

    @OneToMany(mappedBy = "question", orphanRemoval = true, cascade = CascadeType.ALL)
    @Size(min = 2, max = 8, message = "Question can have between 2 and 8 Choices only", groups = {QuestionCreation.class, SurveyCreation.class, SurveyUpdate.class})
    @NotNull(message = "Choices must be specified", groups = {SurveyCreation.class, QuestionCreation.class, SurveyUpdate.class})
    @Valid
    private List<Choice> choices = new ArrayList<>();
}
