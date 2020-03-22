package com.codeit.survey.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private SurveyUser surveyUser;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "survey", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();
}
