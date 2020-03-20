package com.codeit.survey.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(max = 200)
    private String body;

    @ManyToOne
    private Question question;

}
