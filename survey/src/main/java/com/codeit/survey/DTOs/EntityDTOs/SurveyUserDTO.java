package com.codeit.survey.DTOs.EntityDTOs;

import com.codeit.survey.entities.SurveyUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SurveyUserDTO {
    private UUID id;
    private String username;
    private List<AuthorityDTO> authorities;

    public SurveyUserDTO createFromSurveyUser(SurveyUser surveyUser){
        return new SurveyUserDTO(
                surveyUser.getId(),
                surveyUser.getUsername(),
                Arrays.asList(
                        new AuthorityDTO(
                                surveyUser.getAuthority().getId(),
                                surveyUser.getAuthority().getRole())
                )
        );
    }

}
