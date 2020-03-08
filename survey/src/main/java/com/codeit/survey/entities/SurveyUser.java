package com.codeit.survey.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor
@Entity(name = "survey_user")
public class SurveyUser{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    String username;
    String password;
    boolean enabled = true;
    @ManyToOne()
    Authority authority;

    @OneToMany(mappedBy = "surveyUser")
    List<Survey> surveys;

    public SurveyUser(String username, String password, boolean enabled, Authority authority, ArrayList<Survey> surveys) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authority = authority;
        this.surveys = surveys;
    }
}
