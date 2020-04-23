package com.codeit.survey.entities;


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
    private Integer id;

    @Column(nullable = false)
    private boolean published = false;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    @Valid
    private SurveyUser surveyUser;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "survey", orphanRemoval = true, cascade = CascadeType.ALL)
    @Size(min = 1)
    @Valid
    private List<Question> questions = new ArrayList<>();
}
