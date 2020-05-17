package com.codeit.survey.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter @NoArgsConstructor
public class SurveySubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Column(nullable = false)
    LocalDateTime submissionDate;

    @OneToMany(mappedBy = "surveySubmission", cascade = CascadeType.ALL)
    List<QuestionSubmission> questionSubmissions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false)
    SurveyPublication surveyPublication;
}
