package com.codeit.survey.DTOs.EntitiyListDTOs;

import com.codeit.survey.DTOs.EntityDTOs.SurveyUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class SurveyUsersDTO {
    List<SurveyUserDTO> surveyUserList;
}
