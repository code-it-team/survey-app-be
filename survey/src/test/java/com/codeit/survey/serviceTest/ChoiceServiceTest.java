package com.codeit.survey.serviceTest;

import com.codeit.survey.DTOs.EntityDTOs.ChoiceDTO;
import com.codeit.survey.entities.Choice;
import com.codeit.survey.services.ChoiceService;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ChoiceServiceTest {
    @InjectMocks
    private ChoiceService choiceService;

    @Test
     void givenTwoChoices_TwoChoiceDTOsAreCreatedSuccessfully(){
        Choice choice1 = new Choice(1, "Test body", null);
        Choice choice2 = new Choice(2, "Test body2", null);
        List<Choice> choices = Arrays.asList(choice1, choice2);

        List<ChoiceDTO> choiceDTOS = choiceService.createDTOsFromChoices(choices);

        assertEquals(2, choiceDTOS.size());
        assertEquals(choices.get(0).getBody() ,choiceDTOS.get(0).getBody());
        assertEquals(choices.get(0).getId(), choiceDTOS.get(0).getId());

        assertEquals(choices.get(1).getId(), choiceDTOS.get(1).getId());
        assertEquals(choices.get(1).getBody(), choiceDTOS.get(1).getBody());
    }

}
