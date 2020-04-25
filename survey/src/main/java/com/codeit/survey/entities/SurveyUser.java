package com.codeit.survey.entities;


import com.codeit.survey.controllers.validationInterface.SurveyCreation;
import com.codeit.survey.controllers.validationInterface.SurveyUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor
@Entity(name = "survey_user")
public class SurveyUser{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull(message = "User ID can't be empty", groups = {SurveyCreation.class})
    Integer id;

    @Column(unique = true)
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
