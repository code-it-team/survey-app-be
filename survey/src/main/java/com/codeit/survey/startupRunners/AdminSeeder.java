package com.codeit.survey.startupRunners;

import com.codeit.survey.entities.Authority;
import com.codeit.survey.entities.Survey;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.repositories.AuthorityRepo;
import com.codeit.survey.repositories.UserRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AdminSeeder implements CommandLineRunner {
    UserRepo userRepo;
    AuthorityRepo authorityRepo;

    @Autowired
    AdminSeeder(UserRepo userRepo, AuthorityRepo authorityRepo){
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        Authority adminAuthority = new Authority("ROLE_ADMIN", new ArrayList<SurveyUser>());
        Authority userAuthority = new Authority("ROLE_USER", new ArrayList<SurveyUser>());

        SurveyUser jalil = new SurveyUser("jalil.jarjanazy", "testPass", true, adminAuthority, new ArrayList<>());
        SurveyUser hazem = new SurveyUser("hazem.alabiad", "testPass", true, adminAuthority, new ArrayList<>());

        SurveyUser testUser = new SurveyUser("test.test", "testPass", true, userAuthority, new ArrayList<>());



        authorityRepo.saveAll(Arrays.asList(adminAuthority, userAuthority));
        userRepo.saveAll(Arrays.asList(jalil, hazem, testUser));

    }
}
