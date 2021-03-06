package com.codeit.survey.entities;


import com.codeit.survey.controllers.validationInterface.ChoiceCreation;
import com.codeit.survey.controllers.validationInterface.QuestionCreation;
import com.codeit.survey.controllers.validationInterface.SurveyCreation;
import com.codeit.survey.controllers.validationInterface.SurveyUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull(message = "Survey ID can't be null", groups = {ChoiceCreation.class, QuestionCreation.class, SurveyUpdate.class})
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "survey")
    private SurveyPublication surveyPublication;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "A User must be specified", groups = {SurveyCreation.class})
    @Valid
    private SurveyUser surveyUser;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    @NotBlank(message = "Survey name can't be blank", groups = {SurveyCreation.class, SurveyUpdate.class})
    private String name;

    @OneToMany(mappedBy = "survey", orphanRemoval = true, cascade = CascadeType.ALL)
    @Size(min = 1, message = "The Survey must have at least one Question", groups = {SurveyCreation.class, SurveyUpdate.class})
    @Valid
    private List<Question> questions = new ArrayList<>();

    public boolean isPublished() {
        return surveyPublication != null;
    }
}
