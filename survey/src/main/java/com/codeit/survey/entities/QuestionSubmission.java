package com.codeit.survey.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity @Getter @Setter @NoArgsConstructor
public class  QuestionSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    SurveySubmission surveySubmission;

    @OneToOne
    @JoinColumn(nullable = false)
    Question question;

    @OneToOne
    @JoinColumn(nullable = false)
    Choice selectedChoice;

    public QuestionSubmission(Question question, Choice selectedChoice) {
        this.question = question;
        this.selectedChoice = selectedChoice;
    }
}
