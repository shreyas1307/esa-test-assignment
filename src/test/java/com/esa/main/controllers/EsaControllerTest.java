package com.esa.main.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EsaControllerTest {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void save_validFrequency_validAmount_200() throws Exception {

        String regularAmountJson = "{\"frequency\":\"WEEK\", \"amount\":\"1234\"}";

        mockMvc.perform(post("/api/v1/esa")
                .content(regularAmountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is("Data validation successful, and has been saved.")))
                .andExpect(jsonPath("$.status", is("OK")));

    }

    @Test
    public void save_nullFrequency_validAmount_200() throws Exception {

        String regularAmountJson = "{\"frequency\": null, \"amount\": \"1234\"}";

        mockMvc.perform(post("/api/v1/esa")
                        .content(regularAmountJson)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is("Data validation successful, and has been saved.")))
                .andExpect(jsonPath("$.status", is("OK")));

    }

    @Test
    public void save_validFrequency_blankAmount_200() throws Exception {

        String regularAmountJson = "{\"frequency\":\"WEEK\", \"amount\":\" \"}";

        mockMvc.perform(post("/api/v1/esa")
                        .content(regularAmountJson)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is("Data validation successful, and has been saved.")))
                .andExpect(jsonPath("$.status", is("OK")));

    }

    @Test
    public void save_validFrequency_emptyAmount_200() throws Exception {

        String regularAmountJson = "{\"frequency\":\"WEEK\", \"amount\":\"\"}";

        mockMvc.perform(post("/api/v1/esa")
                        .content(regularAmountJson)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is("Data validation successful, and has been saved.")))
                .andExpect(jsonPath("$.status", is("OK")));

    }

    @Test
    public void save_validFrequency_negativeAmount_400() throws Exception {

        String regularAmountJson = "{\"frequency\":\"WEEK\", \"amount\":\"-1234\"}";

        mockMvc.perform(post("/api/v1/esa")
                        .content(regularAmountJson)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is("Amount given is invalid. Value should be a valid whole number. Eg: amount should be greater than 0")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")));

    }

    @Test
    public void save_validFrequency_amountCannotBeSplitWeekly_400() throws Exception {

        String regularAmountJson = "{\"frequency\":\"QUARTER\", \"amount\":\"0\"}";

        mockMvc.perform(post("/api/v1/esa")
                        .content(regularAmountJson)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is("The Amount provided: 0 cannot be divided equally for the Frequency: 13 weeks provided. Please provided a valid number")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")));

    }

}