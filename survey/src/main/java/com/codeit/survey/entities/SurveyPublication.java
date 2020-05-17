package com.codeit.survey.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter  @NoArgsConstructor
public class SurveyPublication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private LocalDateTime publicationDate;

    @OneToOne
    @JoinColumn(unique = true, nullable = false)
    private Survey survey;

    @OneToMany(mappedBy = "surveyPublication", cascade = CascadeType.ALL)
    List<SurveySubmission> surveySubmissionList;

    public SurveyPublication(String link, LocalDateTime publicationDate, Survey survey) {
        this.link = link;
        this.publicationDate = publicationDate;
        this.survey = survey;
    }
}
