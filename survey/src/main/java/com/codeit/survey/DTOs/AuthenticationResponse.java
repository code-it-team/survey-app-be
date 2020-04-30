package com.codeit.survey.DTOs;

import com.codeit.survey.DTOs.EntityDTOs.SurveyUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data @NoArgsConstructor @AllArgsConstructor
public class AuthenticationResponse implements Serializable {
    private String jwt;
    private SurveyUserDTO surveyUser;
}
