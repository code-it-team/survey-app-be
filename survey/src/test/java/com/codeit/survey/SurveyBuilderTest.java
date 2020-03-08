package com.codeit.survey;


import com.codeit.survey.builders.SurveyBuilder;
import com.codeit.survey.entities.Survey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SurveyBuilderTest {
    @Test
    public void SurveyBuilderTest() throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD/mm/yyyy");
        Date date = simpleDateFormat.parse("10/10/2020");

        Survey survey = new SurveyBuilder()
                .setCreationDate(date)
                .setName("Health")
                .buildSurvey();

        assertEquals("Health", survey.getName());
        assertEquals(date, survey.getCreationDate());
    }


}
