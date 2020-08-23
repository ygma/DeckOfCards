package com.example.deckofcards.restfulapi.controller;

import com.example.deckofcards.restfulapi.controller.response.Link;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
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

    protected <TResponse> TResponse callApi(
            HttpMethod httpMethod,
            String url,
            TypeReference<TResponse> typeReference) throws Exception {

        ResultActions resultActions = callApi(httpMethod, url, status().isOk());
        String contentAsString = resultActions
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(contentAsString, typeReference);
    }

    protected ResultActions callApi(HttpMethod httpMethod, String url, ResultMatcher resultMatcher) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = request(httpMethod, url);
        return mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(resultMatcher);
    }

    protected void callApi(Link link) throws Exception {
        callApi(HttpMethod.resolve(link.getType()), link.getHref(), status().isOk());
    }
}
