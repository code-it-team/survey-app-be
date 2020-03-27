package com.codeit.survey.controllers;

import com.codeit.survey.DTOs.EntityDTOs.SurveyUserDTO;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> addUser(@RequestBody SurveyUser surveyUser){
        return userService.addUser(surveyUser);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("admin/user/")
    public ResponseEntity<?> getUser(@RequestBody SurveyUser surveyUser){
        return ResponseEntity.ok(userService.getSurveyUserByUserName(surveyUser.getUsername()));
    }

    @DeleteMapping("admin/user")
    public ResponseEntity<?> deleteUserByUserName(@RequestBody SurveyUser surveyUser){
        return userService.deleteUserByUserName(surveyUser.getUsername());
    }

}
