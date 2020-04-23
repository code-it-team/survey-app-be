package com.codeit.survey.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(max = 1000)
    private String body;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Survey survey;

    @OneToMany(mappedBy = "question", orphanRemoval = true, cascade = CascadeType.ALL)
    @Size(min = 2, max = 8)
    @Valid
    private List<Choice> choices = new ArrayList<>();
}
