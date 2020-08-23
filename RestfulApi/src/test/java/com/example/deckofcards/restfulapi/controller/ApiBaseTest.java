package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.restfulapi.controller.response.LinksResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiBaseTest {

    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    protected void beforeEachBase() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @SneakyThrows
    public void test() {
        LinksResponse<String> actual = callApi("/hello", new TypeReference<LinksResponse<String>>() {});
        assertEquals(new LinksResponse<>("hello world", Collections.emptyList()), actual);
    }

    protected <TResponse> TResponse callApi(String url, TypeReference<TResponse> typeReference) throws Exception {
        String contentAsString = mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(contentAsString, typeReference);
    }
}
