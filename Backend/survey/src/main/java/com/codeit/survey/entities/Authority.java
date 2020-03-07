package com.codeit.survey.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor
@Entity(name = "auth")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Column(unique = true)
    String role;


    @OneToMany(mappedBy = "authority")
    List<SurveyUser> users;

    public Authority(String role, List<SurveyUser> users) {
        this.role = role;
        this.users = users;
    }
}
